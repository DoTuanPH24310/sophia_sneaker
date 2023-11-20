async function exportToExcel() {
    try {
        // Gọi API để nhận tên file từ backend
        const response = await fetch('/exportToExcel');
        const headers = response.headers;
        const disposition = headers.get('Content-Disposition');

        // Trích xuất tên file từ Content-Disposition header
        const filenameMatch = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/.exec(disposition);
        const filename = filenameMatch[1];

        // Chọn thư mục
        const directoryHandle = await window.showDirectoryPicker();

        // Tạo file trong thư mục được chọn
        const fileHandle = await directoryHandle.getFileHandle(filename, { create: true });
        const writableStream = await fileHandle.createWritable();

        // Ghi dữ liệu từ response blob vào file
        const blob = await response.blob();
        await writableStream.write(blob);

        // Đóng stream
        await writableStream.close();
    } catch (error) {
        console.error('Error saving file:', error);
    }
}
