import React from 'react';
import { useDropzone } from 'react-dropzone';

function MyDropzone(props) {
    const { onDrop, accept, text } = props;
    const { getRootProps, getInputProps } = useDropzone({
        onDrop,
        accept
    });

    return (
        <div {...getRootProps({ className: 'dropzone' })} style={{ border: '2px dashed black', padding: '20px', textAlign: 'center' }}>
            <input {...getInputProps()} />
            <p>{text}</p>
        </div>
    );
}

export default MyDropzone;