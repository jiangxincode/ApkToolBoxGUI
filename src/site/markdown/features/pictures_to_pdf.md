## Pictures to PDF Converter

### Introduction
Pictures to PDF Converter is a tool for batch converting image files (JPG, JPEG, PNG) into a single PDF document. It supports recursive search in folders, multi-threaded processing, and allows you to specify the output directory for the generated PDF file.

### Features

- **File Selection**: Add individual image files or entire folders containing images using the file list panel.
- **Target Directory Setting**: Choose the destination folder for the generated PDF file.
- **Recursive Search**: Optionally search all subdirectories for supported image files.
- **Operation Buttons**: Includes "Start" and "Cancel" buttons to begin or interrupt the conversion process.
- **Progress Display**: Shows progress and current status during conversion.

### Usage

1. Add image files or folders to the input panel.
2. Select the target directory for saving the PDF file.
3. (Optional) Enable "Recursive Search" to include images from subfolders.
4. Click the "Start" button to begin converting all selected images into a single PDF file named `result.pdf` in the target directory.
5. Click the "Cancel" button to interrupt the process if needed.
6. Progress and status will be displayed during conversion.

### Notes

- Only JPG, JPEG, and PNG formats are supported.
- The output PDF will have one image per page, and each page size matches the corresponding image size, ensuring no white borders.
- Failed files will be logged with detailed information.
- The target directory must have write permissions, otherwise conversion may fail.
