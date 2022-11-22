import React, { useState } from 'react';

export default function GhDescription() {
  const [description, setDescription] = useState('');
  return (
    <div>
      <p className="font-bold text-lg md:text-lg mb-2.5">숙소 설명</p>
      <textarea
        // ref={ref}
        className="border mr-[15px] h-20 border-border-color mb-3 md:mb-5 rounded-btnRadius w-full h- resize-none pl-[5px] focus:border-border-color focus : border focus:outline-none"
        onChange={(e) => setDescription(e.target.value)}
        value={description}
      />
    </div>
  );
}