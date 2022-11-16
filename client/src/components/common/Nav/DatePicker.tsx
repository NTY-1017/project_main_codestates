import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { ko } from 'date-fns/esm/locale';
const DatePicker1 = () => {
  const [dateRange, setDateRange] = useState([new Date(), null]);
  const [startDate, endDate] = dateRange;
  return (
    <div className="flex justify-center items-center text-base w-datapicker h-search border-solid border-2 border-point rounded-search">
      <DatePicker
        className="mx-4"
        placeholderText="날짜를 선택해주세요"
        locale={ko}
        selectsRange={true}
        startDate={startDate}
        endDate={endDate}
        onChange={(update: any) => {
          setDateRange(update);
        }}
        isClearable={true}
      />
    </div>
  );
};

export default DatePicker1;
