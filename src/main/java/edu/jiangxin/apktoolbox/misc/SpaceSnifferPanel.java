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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SpaceSnifferPanel extends EasyPanel {
    @Serial
    private static final long serialVersionUID = 1L;

    private FilePanel rootDirPanel;
    private JButton scanButton;
    private JButton cancelButton;
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

    private void createTreemapPanel() {
        treemapComponent = new TreemapComponent();
        JScrollPane scrollPane = new JScrollPane(treemapComponent);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Treemap"));
        add(scrollPane);
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
        }

        public void setRoot(TreemapNode root) {
            this.root = root;
            layoutTreemap();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (root == null) {
                g.setColor(Color.GRAY);
                g.drawString("No data. Choose a directory and Scan.", 10, 20);
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            drawNode(g2, root, 0);
            g2.dispose();
        }

        private void drawNode(Graphics2D g2, TreemapNode node, int level) {
            if (node.bounds == null) return;
            boolean hasChildren = node.children != null && !node.children.isEmpty();
            Color base = pickColorForNode(node, level); // removed leaf param
            Color fill = (node == hoverNode) ? lighten(base, 0.22f) : base;
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
            String label = node.name + " - " + humanReadable(node.size);
            FontMetrics fm = g2.getFontMetrics();
            int availableWidth = node.bounds.width - 4;
            int maxChars = Math.max(0, availableWidth / Math.max(1, fm.charWidth('A')));
            if (label.length() > maxChars) {
                label = label.substring(0, Math.max(0, maxChars - 3)) + (maxChars > 3 ? "..." : "");
            }
            int textY = node.bounds.y + fm.getAscent() + 2;
            if (!hasChildren || headerHeight >= fm.getHeight() + 4) {
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

        private Color pickColorForNode(TreemapNode node, int level) { // removed leaf param
            if (level == 0) {
                return new Color(0xE0B784); // root tan
            }
            if (node.file.isDirectory()) {
                // directory hue gradient greenish
                float hue = (0.30f + level * 0.02f) % 1.0f;
                float sat = 0.30f + Math.min(0.25f, level * 0.03f);
                float bri = 0.92f - Math.min(0.45f, level * 0.04f);
                return Color.getHSBColor(hue, sat, bri);
            }
            String ext = getExtension(node.file.getName());
            Color catColor;
            switch (ext) {
                case "jpg": case "jpeg": case "png": case "gif": case "bmp": case "webp":
                    catColor = new Color(0x4FA3A1); // images teal
                    break;
                case "mp4": case "mkv": case "avi": case "mov": case "flv": case "wmv":
                    catColor = new Color(0x9159B8); // video purple
                    break;
                case "mp3": case "wav": case "flac": case "ogg": case "aac": case "m4a":
                    catColor = new Color(0xE0863D); // audio orange
                    break;
                case "zip": case "rar": case "7z": case "tar": case "gz": case "bz2": case "jar": case "war": case "ear":
                    catColor = new Color(0xA0703A); // archive brown
                    break;
                case "pdf":
                    catColor = new Color(0xD04444); // pdf red
                    break;
                case "doc": case "docx": case "xls": case "xlsx": case "ppt": case "pptx": case "txt": case "md": case "html": case "xml": case "rtf":
                    catColor = new Color(0x4F6FB3); // documents blue
                    break;
                case "java": case "class": case "py": case "js": case "ts": case "c": case "cpp": case "h": case "hpp": case "cs": case "rb": case "go": case "rs": case "php": case "kt": case "swift": case "sh": case "bat":
                    catColor = new Color(0x4F9E4F); // code green
                    break;
                case "exe": case "dll": case "so": case "bin": case "ps1":
                    catColor = new Color(0xC63F3F); // executables strong red
                    break;
                default:
                    catColor = new Color(0x7A7A7A); // other gray
            }
            // adjust brightness by level for depth perception
            float factor = 1.0f - Math.min(0.35f, level * 0.05f);
            return scaleBrightness(catColor, factor);
        }

        private String getExtension(String name) {
            int idx = name.lastIndexOf('.') ;
            if (idx < 0 || idx == name.length()-1) return "";
            return name.substring(idx+1).toLowerCase();
        }

        private Color scaleBrightness(Color c, float factor) {
            int r = Math.min(255, Math.max(0, (int)(c.getRed() * factor)));
            int g = Math.min(255, Math.max(0, (int)(c.getGreen() * factor)));
            int b = Math.min(255, Math.max(0, (int)(c.getBlue() * factor)));
            return new Color(r, g, b);
        }

        private Color lighten(Color c, float add) {
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
            if (root == null) return;
            Dimension d = getSize();
            if (d.width <= 0 || d.height <= 0) return;
            root.bounds = new Rectangle(0, 0, d.width, d.height);
            if (root.children == null || root.children.isEmpty()) return;
            layoutChildren(root, 0, true);
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
}
