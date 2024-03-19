import React, { useState } from 'react';
import Switch from 'react-switch';

function MyToggle() {
  const [checked, setChecked] = useState(false);

  const handleChange = (nextChecked) => {
    setChecked(nextChecked);
  };

  return (
    <label>
      <span>Hide</span>
      <Switch
        onChange={handleChange}
        checked={checked}
        offColor="#888"
        onColor="#4D4D4D"
        uncheckedIcon={false}
        checkedIcon={false}
        height={20}
        width={48}
        handleDiameter={24}
      />
      <span>Reveal</span>
    </label>
  );
}

export default MyToggle;
