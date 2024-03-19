import React, { useCallback, useState } from 'react';
import { useDropzone } from 'react-dropzone';
import file from './file.svg';
import './MyDropzone.css'; // Make sure to import the CSS file for styling

const MyDropzone = ({ onDrop, accept, text }) => {
    const [filePreview, setFilePreview] = useState({ previewSrc: '', isImage: true, filename: '' });

    const onDropAccepted = useCallback((acceptedFiles) => {
        // Call the onDrop function passed from the parent component
        onDrop?.(acceptedFiles);

        const file = acceptedFiles[0];
        const reader = new FileReader();
        reader.onloadend = () => {
            setFilePreview({
                previewSrc: reader.result,
                isImage: file.type.startsWith('image/'),
                filename: file.name
            });
        };

        if (file.type.startsWith('image/')) {
            reader.readAsDataURL(file);
        } else {
            // For non-image files, you can set a default thumbnail or icon
            setFilePreview({
                previewSrc: 'file', // This should be a valid path to a default icon
                isImage: false,
                filename: file.name
            });
        }
    }, [onDrop]);

    const { getRootProps, getInputProps } = useDropzone({
        onDropAccepted,
        accept
    });

    return (
        <div {...getRootProps()} className="dropzone">
            <input {...getInputProps()} />
            {filePreview.previewSrc && (
                filePreview.isImage ? (
                    <div className="file-info">
                        <img src={filePreview.previewSrc} alt={`Preview of ${filePreview.filename}`} className="preview-image" />
                        <p>{filePreview.filename}</p> {/* Display the file name */}
                    </div>
                ) : (
                    <div className="file-info">
                        <img src={file} className="preview-image" alt="file" />{/* Here you can add an icon or thumbnail for non-image files */}
                        <p>{filePreview.filename}</p> {/* Display the file name */}
                    </div>
                )
            )}
            <p>{text}</p>
        </div >
    );
};

export default MyDropzone;
