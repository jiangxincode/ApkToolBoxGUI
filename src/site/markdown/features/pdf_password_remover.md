## PDF Password Remover Feature Documentation

### Introduction
PDF Password Remover is used to batch remove password protection from PDF files. It supports recursive search for all PDF files in directories and saves the processed files to a specified target folder.

### Features

- **File Selection**: Add files or folders to be processed via the file list panel.
- **Target Directory Setting**: Choose the destination folder for processed PDF files.
- **Recursive Search**: Optional, supports searching all subdirectories for PDF files.
- **Operation Buttons**: Includes "Start" and "Cancel" buttons to begin or interrupt the process.
- **Progress Display**: Shows a progress bar and current status during processing.

### Usage

1. Add PDF files or folders to the input panel for password removal.
2. Select the target directory for saving processed files.
3. Check the "Recursive Search" option if needed.
4. Click the "Start" button to automatically find and process all PDF files, removing password protection.
5. You can click the "Cancel" button to interrupt the process.
6. The progress bar will display real-time progress and status.

### Notes

- Multi-threaded processing is supported, suitable for large batch operations.
- Failed files will be logged with detailed information.
- The target directory must have write permissions, otherwise processing may fail.
