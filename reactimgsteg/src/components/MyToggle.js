import React, { useState } from 'react';
import './MyToggle.css'; // Make sure to import the CSS file for styling

const MyToggle = ({ onToggle }) => {
    const [isToggled, setIsToggled] = useState(false);

    const toggleSwitch = () => {
        setIsToggled(!isToggled);
        if (onToggle) { onToggle(!isToggled); }
        // Log the current state of the toggle
        // true = Hide, false = Reveal
        console.log('The toggle is now', isToggled ? 'Hide' : 'Reveal');
    };

    return (
        <div className="toggle-switch" onClick={toggleSwitch}>
            <div className="toggle-label-reveal">Reveal</div>
            <div className="toggle-label-hide">Hide</div>
            <div className={`toggle-btn ${isToggled ? 'toggled' : ''}`} >
                <div className="toggle-btn-inner">
                    <div className="toggle-text">{isToggled ? 'Reveal' : 'Hide'}</div>
                </div>
            </div>
        </div>
    );
};

export default MyToggle;
