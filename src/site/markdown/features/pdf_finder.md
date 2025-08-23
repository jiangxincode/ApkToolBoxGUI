## PDF Finder Feature Documentation

### Introduction
PDF Finder is used to batch search for PDF files in specified directories and filter results based on various conditions. It supports recursive search of subdirectories and can filter scanned files, encrypted files, files without outlines, and files with annotations.

### Features

- **File Selection**: Add folders or files to be searched via the file list panel.
- **Search Types**:
  - Find scanned PDF files (threshold adjustable)
  - Find encrypted PDF files
  - Find PDF files without outlines
  - Find PDF files with annotations
- **Threshold Setting**: Only available when searching for scanned PDFs, used to adjust detection sensitivity.
- **Recursive Search**: Optionally search subfolders recursively.
- **Progress Display**: Shows progress bar and status during search.
- **Result Display**: Results are shown in a table, including file index, parent directory, file name, size, and modification time.
- **Right-click Actions**: Right-click in the result table to open the parent folder of a file.

### Usage

1. In the "Option" tab, add folders or files to be searched.
2. Select the search type and set threshold and recursion options as needed.
3. Click the "Search" button to start searching; the progress bar will show the current progress.
4. After the search is complete, it will automatically switch to the "Result" tab to view results.
5. In the result table, right-click and select "Open parent folder of this file" to open the file's location.

### Notes

- You can click the "Cancel" button to interrupt the search process.
- Multi-threading is supported for faster processing, suitable for large batch searches.
- Only PDF files are supported (extensions .pdf or .PDF).
