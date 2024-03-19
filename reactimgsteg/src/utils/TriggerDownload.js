export function triggerDownload(file) {
    // Create a URL for the file
    const fileUrl = URL.createObjectURL(file);

    // Create a temporary anchor element and set its attributes
    const a = document.createElement('a');
    document.body.appendChild(a);
    a.style = 'display: none';
    a.href = fileUrl;
    a.download = file.name;

    // Trigger the download by simulating a click
    a.click();

    // Clean up by revoking the object URL and removing the anchor
    window.URL.revokeObjectURL(fileUrl);
    document.body.removeChild(a);
}
