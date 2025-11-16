package edu.jiangxin.apktoolbox.misc;

import edu.jiangxin.apktoolbox.swing.extend.EasyPanel;
import edu.jiangxin.apktoolbox.swing.extend.filepanel.FilePanel;
import edu.jiangxin.apktoolbox.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.io.File;
import java.io.Serial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpaceSnifferPanel extends EasyPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private FilePanel rootDirPanel;
    private JButton scanButton;
    private JButton cancelButton;
    // navigation buttons
    private JButton backButton;
    private JButton forwardButton;
    private JButton rootButton;
    private JLabel focusPathLabel;
    private JCheckBox showFilesCheckBox;
    private JCheckBox followSymlinkCheckBox;
    private JSpinner depthSpinner;
    private JSpinner minSizeSpinner; // MB
    private TreemapComponent treemapComponent;

    private transient ScanWorker scanWorker;

    @Override
    public void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createInputPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createOptionPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createOperationPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createNavigationPanel();
        add(Box.createVerticalStrut(Constants.DEFAULT_Y_BORDER));
        createTreemapPanel();
    }

    private void createInputPanel() {
        rootDirPanel = new FilePanel("Root Directory");
        rootDirPanel.initialize();
        rootDirPanel.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        add(rootDirPanel);
    }

    private void createOptionPanel() {
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.X_AXIS));
        optionPanel.setBorder(BorderFactory.createTitledBorder("Options"));

        showFilesCheckBox = new JCheckBox("Include Files", true);
        followSymlinkCheckBox = new JCheckBox("Follow Symlinks", false);
        depthSpinner = new JSpinner(new SpinnerNumberModel(6, 1, 32, 1));
        minSizeSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 1024, 1)); // MB threshold

        optionPanel.add(new JLabel("Max Depth:"));
        optionPanel.add(depthSpinner);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(new JLabel("Min Size(MB):"));
        optionPanel.add(minSizeSpinner);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(showFilesCheckBox);
        optionPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        optionPanel.add(followSymlinkCheckBox);
        optionPanel.add(Box.createHorizontalGlue());
        add(optionPanel);
    }

    private void createOperationPanel() {
        JPanel operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.X_AXIS));
        operationPanel.setBorder(BorderFactory.createTitledBorder("Operations"));
        scanButton = new JButton("Scan");
        cancelButton = new JButton("Cancel");
        cancelButton.setEnabled(false);
        scanButton.addActionListener(e -> startScan());
        cancelButton.addActionListener(e -> cancelScan());
        operationPanel.add(scanButton);
        operationPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        operationPanel.add(cancelButton);
        operationPanel.add(Box.createHorizontalGlue());
        add(operationPanel);
    }

    private void createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
        navPanel.setBorder(BorderFactory.createTitledBorder("Navigation"));
        backButton = new JButton("←");
        forwardButton = new JButton("→");
        rootButton = new JButton("Root");
        backButton.setEnabled(false);
        forwardButton.setEnabled(false);
        rootButton.setEnabled(false);
        focusPathLabel = new JLabel("Focus: (none)");
        backButton.addActionListener(e -> {
            treemapComponent.goBack();
            updateNavigationButtons();
        });
        forwardButton.addActionListener(e -> {
            treemapComponent.goForward();
            updateNavigationButtons();
        });
        rootButton.addActionListener(e -> {
            treemapComponent.focusRoot();
            updateNavigationButtons();
        });
        navPanel.add(backButton);
        navPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        navPanel.add(forwardButton);
        navPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        navPanel.add(rootButton);
        navPanel.add(Box.createHorizontalStrut(Constants.DEFAULT_X_BORDER));
        navPanel.add(focusPathLabel);
        navPanel.add(Box.createHorizontalGlue());
        add(navPanel);
    }

    private void createTreemapPanel() {
        treemapComponent = new TreemapComponent();
        treemapComponent.setFocusChangeListener(node -> updateNavigationButtons());
        JScrollPane scrollPane = new JScrollPane(treemapComponent);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Treemap"));
        add(scrollPane);
    }

    private void updateNavigationButtons() {
        backButton.setEnabled(treemapComponent.isBackAvailable());
        forwardButton.setEnabled(treemapComponent.isForwardAvailable());
        rootButton.setEnabled(!treemapComponent.hasRootFocused() && treemapComponent.getRoot() != null);
        String path = treemapComponent.getFocusPath();
        focusPathLabel.setText("Focus: " + (path == null ? "(none)" : path));
    }

    private void startScan() {
        if (scanWorker != null && scanWorker.isRunning()) {
            return;
        }
        File root = rootDirPanel.getFile();
        if (root == null || !root.isDirectory()) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Please choose a valid root directory", Constants.MESSAGE_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
            rootDirPanel.requestFocusInWindow();
            return;
        }
        scanButton.setEnabled(false);
        cancelButton.setEnabled(true);
        rootButton.setEnabled(false);
        treemapComponent.setRoot(null);
        treemapComponent.repaint();
        int depth = (Integer) depthSpinner.getValue();
        long minSizeBytes = ((Integer) minSizeSpinner.getValue()) * 1024L * 1024L;
        boolean includeFiles = showFilesCheckBox.isSelected();
        boolean followSymlink = followSymlinkCheckBox.isSelected();
        scanWorker = new ScanWorker(root, depth, minSizeBytes, includeFiles, followSymlink);
        scanWorker.start();
    }

    private void cancelScan() {
        if (scanWorker != null) {
            scanWorker.cancel();
        }
    }

    class ScanWorker extends Thread {
        private final File root;
        private final int depthLimit;
        private final long minSizeBytes;
        private final boolean includeFiles;
        private final boolean followSymlinks;
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private volatile boolean running = false;

        public ScanWorker(File root, int depthLimit, long minSizeBytes, boolean includeFiles, boolean followSymlinks) {
            super("SpaceSnifferScanWorker");
            this.root = root;
            this.depthLimit = depthLimit;
            this.minSizeBytes = minSizeBytes;
            this.includeFiles = includeFiles;
            this.followSymlinks = followSymlinks;
        }

        public boolean isRunning() { return running; }

        public void cancel() { cancelled.set(true); interrupt(); }

        @Override
        public void run() {
            running = true;
            try {
                TreemapNode node = buildTree(root, 0, depthLimit, minSizeBytes, includeFiles, followSymlinks, cancelled);
                if (cancelled.get()) { return; }
                SwingUtilities.invokeLater(() -> {
                    treemapComponent.setRoot(node);
                    treemapComponent.focusRoot();
                    updateNavigationButtons();
                    treemapComponent.revalidate();
                    treemapComponent.repaint();
                });
            } catch (Exception ex) {
                logger.error("Scan failed", ex);
            } finally {
                running = false;
                SwingUtilities.invokeLater(() -> {
                    scanButton.setEnabled(true);
                    cancelButton.setEnabled(false);
                });
            }
        }
    }

    // Recursive building of tree
    private TreemapNode buildTree(File file, int depth, int depthLimit, long minSizeBytes, boolean includeFiles,
                                  boolean followSymlinks, AtomicBoolean cancelled) {
        if (cancelled.get()) { return null; }
        long size = 0L;
        List<TreemapNode> children = new ArrayList<>();
        boolean isDir = file.isDirectory();
        Path path = file.toPath();
        boolean isSymlink = Files.isSymbolicLink(path);
        if (isSymlink && !followSymlinks) {
            return null; // skip symlink if not following
        }
        if (isDir && depth < depthLimit) {
            File[] list = file.listFiles();
            if (list != null) {
                for (File child : list) {
                    if (cancelled.get()) { return null; }
                    if (!child.canRead()) { continue; }
                    TreemapNode childNode = buildTree(child, depth + 1, depthLimit, minSizeBytes, includeFiles, followSymlinks, cancelled);
                    if (childNode != null) {
                        size += childNode.size;
                        children.add(childNode);
                    }
                }
            }
        }
        if (!isDir) {
            if (!includeFiles) { return null; }
            size = file.length();
        } else if (depth == depthLimit) {
            // leaf directory at depth limit: approximate size by file lengths directly
            File[] list = file.listFiles();
            if (list != null) {
                for (File child : list) {
                    if (cancelled.get()) { return null; }
                    if (child.isFile()) { size += child.length(); }
                }
            }
        }
        if (size < minSizeBytes) { return null; }
        return new TreemapNode(file.getName().isEmpty() ? file.getPath() : file.getName(), file, size, children);
    }

    // Treemap node structure
    static class TreemapNode {
        final String name;
        final File file;
        final long size; // bytes
        final List<TreemapNode> children;
        Rectangle bounds; // assigned during layout

        TreemapNode(String name, File file, long size, List<TreemapNode> children) {
            this.name = name;
            this.file = file;
            this.size = size;
            this.children = children;
        }
    }

    // Component that renders treemap
    static class TreemapComponent extends JComponent {
        @Serial
        private static final long serialVersionUID = 1L;
        private TreemapNode root; // full tree root
        private TreemapNode hoverNode; // node currently under mouse for highlight
        // navigation stacks
        private final Deque<TreemapNode> backStack = new LinkedList<>();
        private final Deque<TreemapNode> forwardStack = new LinkedList<>();
        private TreemapNode focus; // current focused node for view
        private FocusChangeListener focusChangeListener;
        private final JPopupMenu popupMenu = new JPopupMenu();
        private TreemapNode popupTarget;
        private static final Logger LOGGER = LogManager.getLogger(TreemapComponent.class.getSimpleName());

        public TreemapComponent() {
            setPreferredSize(new Dimension(800, 600));
            setOpaque(true);
            setBackground(Color.WHITE);
            // Remove focus navigation – full tree always displayed
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    layoutTreemap();
                    repaint();
                }
            });
            ToolTipManager.sharedInstance().registerComponent(this);
            ToolTipManager.sharedInstance().setInitialDelay(300);
            ToolTipManager.sharedInstance().setDismissDelay(15000);
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    if (root == null) { hoverNode = null; return; }
                    TreemapNode newHover = findDeepByPoint(root, e.getPoint());
                    if (newHover != hoverNode) {
                        hoverNode = newHover;
                        repaint();
                    }
                    // trigger tooltip refresh
                    setToolTipText(getToolTipText(e));
                }
            });
            // Initialize context menu actions
            JMenuItem openDirItem = new JMenuItem("Open Directory");
            openDirItem.addActionListener(ev -> {
                if (popupTarget != null) {
                    File dir = popupTarget.file.isDirectory() ? popupTarget.file : popupTarget.file.getParentFile();
                    openDirectory(dir);
                }
            });
            JMenuItem copyPathItem = new JMenuItem("Copy Path");
            copyPathItem.addActionListener(ev -> {
                if (popupTarget != null) {
                    copyToClipboard(popupTarget.file.getAbsolutePath());
                }
            });
            JMenuItem copyNameItem = new JMenuItem("Copy Name");
            copyNameItem.addActionListener(ev -> {
                if (popupTarget != null) {
                    copyToClipboard(popupTarget.name);
                }
            });
            popupMenu.add(openDirItem);
            popupMenu.add(copyPathItem);
            popupMenu.add(copyNameItem);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) { maybeShowPopup(e); }
                @Override
                public void mouseReleased(MouseEvent e) { maybeShowPopup(e); }
                private void maybeShowPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        if (root == null) { return; }
                        popupTarget = findDeepByPoint(root, e.getPoint());
                        if (popupTarget != null) {
                            popupMenu.show(TreemapComponent.this, e.getX(), e.getY());
                        }
                    }
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && !SwingUtilities.isRightMouseButton(e)) {
                        if (focus != null) {
                            TreemapNode target = findDeepByPoint(focus, e.getPoint());
                            if (target != null && target != focus) {
                                focusOn(target);
                            }
                        }
                    }
                }
            });
        }

        public void setRoot(TreemapNode root) {
            this.root = root;
            backStack.clear();
            forwardStack.clear();
            focus = root;
            layoutTreemap();
            fireFocusChanged();
        }

        // focus controls
        public void focusRoot() {
            if (root == null) return;
            if (focus != root) {
                backStack.push(focus);
                forwardStack.clear();
                focus = root;
            }
            layoutTreemap();
            repaint();
            fireFocusChanged();
        }

        public void focusOn(TreemapNode node) {
            if (node == null || node == focus) return;
            backStack.push(focus);
            forwardStack.clear();
            focus = node;
            layoutTreemap();
            repaint();
            fireFocusChanged();
        }

        public void goBack() {
            if (backStack.isEmpty()) return;
            forwardStack.push(focus);
            focus = backStack.pop();
            layoutTreemap();
            repaint();
            fireFocusChanged();
        }

        public void goForward() {
            if (forwardStack.isEmpty()) return;
            backStack.push(focus);
            focus = forwardStack.pop();
            layoutTreemap();
            repaint();
            fireFocusChanged();
        }

        public boolean isBackAvailable() { return !backStack.isEmpty(); }
        public boolean isForwardAvailable() { return !forwardStack.isEmpty(); }
        public boolean hasRootFocused() { return focus == root; }
        public TreemapNode getRoot() { return root; }
        public String getFocusPath() { return (focus == null) ? null : focus.file.getAbsolutePath(); }

        public void setFocusChangeListener(FocusChangeListener l) { this.focusChangeListener = l; }
        private void fireFocusChanged() { if (focusChangeListener != null) focusChangeListener.onFocusChanged(focus); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (focus == null) {
                g.setColor(Color.GRAY);
                g.drawString("No data. Choose a directory and Scan.", 10, 20);
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawNode(g2, focus, 0);
            g2.dispose();
        }

        private void drawNode(Graphics2D g2, TreemapNode node, int level) {
            if (node.bounds == null) return;
            boolean hasChildren = node.children != null && !node.children.isEmpty();
            boolean isDir = node.file.isDirectory();
            Color base = pickColorForNode(node, level);
            Color fill = (node == hoverNode) ? lighten(base) : base;
            g2.setColor(fill);
            g2.fillRect(node.bounds.x, node.bounds.y, node.bounds.width, node.bounds.height);
            g2.setColor(Color.BLACK);
            if (node == hoverNode) {
                Stroke old = g2.getStroke();
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRect(node.bounds.x, node.bounds.y, node.bounds.width, node.bounds.height);
                g2.setStroke(old);
            } else {
                g2.drawRect(node.bounds.x, node.bounds.y, node.bounds.width, node.bounds.height);
            }

            int headerHeight = computeHeaderHeight(node.bounds);
            if (hasChildren && headerHeight > 0) {
                g2.setColor(fill.darker());
                g2.fillRect(node.bounds.x, node.bounds.y, node.bounds.width, headerHeight);
                g2.setColor(Color.BLACK);
                g2.drawLine(node.bounds.x, node.bounds.y + headerHeight, node.bounds.x + node.bounds.width, node.bounds.y + headerHeight);
            }

            // Build label: directories always show name; files show name + size.
            String label;
            if (isDir) {
                label = node.name; // keep concise for directory
            } else {
                label = node.name + " - " + humanReadable(node.size);
            }
            FontMetrics fm = g2.getFontMetrics();
            int availableWidth = node.bounds.width - 4;
            int maxChars = Math.max(0, availableWidth / Math.max(1, fm.charWidth('A')));
            if (label.length() > maxChars) {
                label = label.substring(0, Math.max(0, maxChars - 3)) + (maxChars > 3 ? "..." : "");
            }
            int textY = node.bounds.y + fm.getAscent() + 2;
            // Draw rules:
            // 1. Directory: always draw if there's vertical space for ascent (even if headerHeight == 0 or too small)
            // 2. File: draw only if enough header or no children (leaf)
            boolean canDraw = true;
            if (!isDir) {
                canDraw = (!hasChildren) || (headerHeight >= fm.getHeight() + 4);
            }
            if (canDraw && fm.getHeight() + 4 <= node.bounds.height) {
                g2.drawString(label, node.bounds.x + 2, textY);
            }

            if (hasChildren) {
                for (TreemapNode child : node.children) {
                    drawNode(g2, child, level + 1);
                }
            }
        }

        private int computeHeaderHeight(Rectangle r) {
            if (r.height < 24 || r.width < 40) return 0; // too small, skip header
            return Math.min(18, r.height / 5);
        }

        private Color pickColorForNode(TreemapNode node, int level) { // softened pastel palette
            // Define base pastel colors
            Color ROOT = new Color(0xF5D4A6); // soft tan
            Color DIR = new Color(0xCFE8C9); // light green
            Color IMG = new Color(0xBDE3E6); // light teal
            Color VID = new Color(0xD7C5ED); // light purple
            Color AUD = new Color(0xF8D1AC); // light orange
            Color ARC = new Color(0xE2C6A8); // light brown
            Color PDF = new Color(0xF6B5B5); // soft pink red
            Color DOC = new Color(0xC6D8F5); // soft blue
            Color CODE = new Color(0xC8E6C8); // soft green
            Color EXE = new Color(0xF5A3A3); // soft red
            Color OTHER = new Color(0xD2D2D2); // light gray

            if (level == 0) {
                return ROOT;
            }
            if (node.file.isDirectory()) {
                return adjustByLevel(DIR, level);
            }
            String ext = getExtension(node.file.getName());
            Color base;
            switch (ext) {
                case "jpg": case "jpeg": case "png": case "gif": case "bmp": case "webp":
                    base = IMG; break;
                case "mp4": case "mkv": case "avi": case "mov": case "flv": case "wmv":
                    base = VID; break;
                case "mp3": case "wav": case "flac": case "ogg": case "aac": case "m4a":
                    base = AUD; break;
                case "zip": case "rar": case "7z": case "tar": case "gz": case "bz2": case "jar": case "war": case "ear":
                    base = ARC; break;
                case "pdf":
                    base = PDF; break;
                case "doc": case "docx": case "xls": case "xlsx": case "ppt": case "pptx": case "txt": case "md": case "html": case "xml": case "rtf":
                    base = DOC; break;
                case "java": case "class": case "py": case "js": case "ts": case "c": case "cpp": case "h": case "hpp": case "cs": case "rb": case "go": case "rs": case "php": case "kt": case "swift": case "sh": case "bat":
                    base = CODE; break;
                case "exe": case "dll": case "so": case "bin": case "ps1":
                    base = EXE; break;
                default:
                    base = OTHER; break;
            }
            return adjustByLevel(base, level);
        }

        private Color adjustByLevel(Color base, int level) {
            // Slightly darken deeper levels to preserve hierarchy (max 30%)
            float factor = 1.0f - Math.min(0.30f, level * 0.04f);
            int r = Math.round(base.getRed() * factor);
            int g = Math.round(base.getGreen() * factor);
            int b = Math.round(base.getBlue() * factor);
            return new Color(r, g, b);
        }

        private String getExtension(String name) {
            int idx = name.lastIndexOf('.');
            if (idx < 0 || idx == name.length() - 1) return "";
            return name.substring(idx + 1).toLowerCase(Locale.ROOT);
        }

        private Color lighten(Color c) { // reduce highlight strength fixed factor
            float add = 0.12f;
            int r = Math.min(255, (int)(c.getRed() + 255 * add));
            int g = Math.min(255, (int)(c.getGreen() + 255 * add));
            int b = Math.min(255, (int)(c.getBlue() + 255 * add));
            return new Color(r, g, b);
        }

        @Override
        public String getToolTipText(MouseEvent event) {
            if (root == null) return null;
            TreemapNode node = findDeepByPoint(root, event.getPoint());
            if (node == null) return null;
            double percent = root.size > 0 ? (node.size * 100.0 / root.size) : 0.0;
            String type = (node.children != null && !node.children.isEmpty()) ? "Directory" : (node.file.isDirectory() ? "Directory" : "File");
            int childCount = (node.children == null) ? 0 : node.children.size();
            String path = node.file.getPath();
            return "<html><b>" + escapeHtml(node.name) + "</b><br/>" +
                    "Path: " + escapeHtml(path) + "<br/>" +
                    "Type: " + type + (childCount > 0 ? " (children: " + childCount + ")" : "") + "<br/>" +
                    "Size: " + humanReadable(node.size) + "<br/>" +
                    String.format("Percent of root: %.2f%%", percent) + "</html>";
        }

        private TreemapNode findDeepByPoint(TreemapNode node, Point p) {
            if (node == null || node.bounds == null || !node.bounds.contains(p)) return null;
            if (node.children != null) {
                for (TreemapNode c : node.children) {
                    TreemapNode r = findDeepByPoint(c, p);
                    if (r != null) return r;
                }
            }
            return node;
        }

        private String escapeHtml(String s) {
            if (s == null) return "";
            return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        }

        private String humanReadable(long bytes) {
            if (bytes < 1024) return bytes + "B";
            int exp = (int) (Math.log(bytes) / Math.log(1024));
            char pre = "KMGTPE".charAt(exp - 1);
            return String.format("%.1f%s", bytes / Math.pow(1024, exp), pre);
        }

        private void layoutTreemap() {
            if (focus == null) return;
            Dimension d = getSize();
            if (d.width <= 0 || d.height <= 0) return;
            focus.bounds = new Rectangle(0, 0, d.width, d.height);
            if (focus.children == null || focus.children.isEmpty()) return;
            layoutChildren(focus, 0, true);
        }

        private void layoutChildren(TreemapNode parent, int level, boolean horizontal) {
            if (parent.children == null || parent.children.isEmpty()) return;
            Rectangle parentRect = parent.bounds;
            int headerHeight = computeHeaderHeight(parentRect);
            Rectangle contentArea = new Rectangle(parentRect.x, parentRect.y + headerHeight,
                    parentRect.width, parentRect.height - headerHeight);
            long total = 0L; for (TreemapNode c : parent.children) total += c.size;
            if (total <= 0) return;
            slice(contentArea, parent.children, total, horizontal);
            for (TreemapNode c : parent.children) {
                layoutChildren(c, level + 1, !horizontal);
            }
        }

        private void slice(Rectangle area, List<TreemapNode> nodes, long totalSize, boolean horizontal) {
            int x = area.x; int y = area.y; int w = area.width; int h = area.height;
            for (int i = 0; i < nodes.size(); i++) {
                TreemapNode n = nodes.get(i);
                double ratio = (double) n.size / (double) totalSize;
                if (horizontal) {
                    int width = (int) Math.round(w * ratio);
                    if (i == nodes.size() - 1) width = area.x + w - x;
                    n.bounds = new Rectangle(x, y, width, h);
                    x += width;
                } else {
                    int height = (int) Math.round(h * ratio);
                    if (i == nodes.size() - 1) height = area.y + h - y;
                    n.bounds = new Rectangle(x, y, w, height);
                    y += height;
                }
            }
        }

        private void openDirectory(File dir) {
            if (dir == null) { return; }
            try {
                if (!dir.exists()) { LOGGER.warn("Directory does not exist: {}", dir); return; }
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        desktop.open(dir);
                        return;
                    }
                }
                // Fallback per OS
                String os = System.getProperty("os.name").toLowerCase();
                Process process;
                if (os.contains("win")) {
                    process = new ProcessBuilder("explorer", dir.getAbsolutePath()).start();
                } else if (os.contains("mac")) {
                    process = new ProcessBuilder("open", dir.getAbsolutePath()).start();
                } else {
                    process = new ProcessBuilder("xdg-open", dir.getAbsolutePath()).start();
                }
                process.waitFor();
            } catch (Exception ex) {
                LOGGER.error("Open directory failed", ex);
            }
        }

        private void copyToClipboard(String text) {
            try {
                StringSelection selection = new StringSelection(text);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            } catch (Exception ex) {
                LOGGER.error("Copy to clipboard failed", ex);
            }
        }
    }

    interface FocusChangeListener {
        void onFocusChanged(TreemapNode focusNode);
    }
}
