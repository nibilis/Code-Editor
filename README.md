# Simple Text Editor

This project is a simple command-line text editor implemented in Java using a circular doubly linked list. The editor provides basic file operations and text manipulation commands, resembling a lightweight version of `vim` or `nano`. There are various commands allow the user to manipulate and edit the content.

## Features

- **Open File** (`:e filename`): Opens a text file and stores each line as a node in the circular doubly linked list.
- **Save File** (`:w` or `:w filename`): Saves the current content to the opened file or to a new file.
- **Exit Editor** (`:q!`): Exits the editor with an option to save unsaved changes.
- **Copy & Paste**:
  - Mark text with `:v startLine endLine`
  - Copy marked text with `:y`
  - Cut marked text with `:c`
  - Paste clipboard content with `:p lineNumber`
- **Text Insertion**:
  - Append text after a specific line with `:a lineNumber`
  - Insert text before a specific line with `:i lineNumber`
- **Delete Lines**:
  - Delete a specific line with `:x lineNumber`
  - Delete lines from a specific line to the end with `:xG lineNumber`
  - Delete lines from the beginning to a specific line with `:XG lineNumber`
- **Search & Replace**:
  - Search for text with `:/ element`
  - Replace text globally with `:/ element replacement`
  - Replace text in a specific line with `:/ element replacement lineNumber`
- **Display Content** (`:s` or `:s startLine endLine`): Shows the content of the file, displaying 20 lines at a time.
- **Help** (`:help`): Displays a list of available commands.

## Authors

- Marco Antonio de Camargo - RA 10418309
- Natan Moreira Passos - RA 10417916
- Nicolas Henriques de Almeida - RA 10418357
