import React, { useEffect, useState } from 'react';
import CommonBtn from '../common/CommonBtn/CommonBtn';
import axios from 'axios';
type ReservationType = {
  guestHouserName: string;
  guestHouseRoomStart: string;
  guestHouseRoomEnd: string;
  guestHouseDays: number;
  guestHousePrice: number;
};
const ReservationInfo = () => {
  const [reservationData, setReservationData] = useState<ReservationType>();
  const [price, setPrice] = useState<number>(10000);
  //임시로 구현 해 두었습니다
  const getReservationData = async () => {
    setReservationData({
      guestHouserName: '팜파스트호텔 제주',
      guestHouseRoomStart: '2022.11.14 ',
      guestHouseRoomEnd: '2022.11.15 ',
      guestHouseDays: 5,
      guestHousePrice: 10000,
    });
    const Data = await axios.get('data');
  };

  useEffect(() => {
    getReservationData();
  }, []);

  const handleToMain = () => {
    //메인으로 이동
    console.log('/');
  };

  return (
    <div className="flex justify-center items-center h-screen ">
      <div className="lg:p-0 lg:flex lg:max-w-[1150px] lg:h-[310px] sm:p-[40px] sm:p-[80px]">
        <div className="w-full ">
          <img
            className=" rounded-[10px]  bg-center bg-no-repeat bg-cover flex-none lg:w-[1150px] lg:h-[310px]"
            src=" https://a0.muscache.com/im/pictures/miso/Hosting-713898202877836679/original/7b073b89-ffea-47f9-a8b3-e6ccd96f0f16.jpeg?im_w=1200"
          ></img>
        </div>
        <div className="lg:ml-[60px] mt-[24px] text-lg w-full">
          <div className="font-semibold	text-xl ">
            {reservationData && reservationData.guestHouserName}
          </div>
          {/* <div className="text-font-color mt-[20px]">2층 201호 6인실(남성)</div> */}
          <div className="mt-[10px]">
            {`${reservationData && reservationData.guestHouseRoomStart} ~ ${
              reservationData && reservationData.guestHouseRoomEnd
            }`}
            <div />
            <div>
              게스트 1명
              <div />
              <div className="font-semibold mt-[20px] ">요금 세부 정보</div>
              <div className="mt-[10px]">
                {`￦${reservationData && reservationData.guestHousePrice}`} X{' '}
                {reservationData && reservationData.guestHouseDays} 박
              </div>
              <div className="flex justify-between items-center ">
                <span className="font-semibold mt-[10px]">총 합계</span>
                <span className="text-base pr-[60px] ">{price}</span>
              </div>
              <div className="flex justify-center mt-[15px]">
                <CommonBtn
                  text={'예약 확인'}
                  btnSize={'w-[235px] h-[47px]'}
                  btnHandler={handleToMain}
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReservationInfo;
