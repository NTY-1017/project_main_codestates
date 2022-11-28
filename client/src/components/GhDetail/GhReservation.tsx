import React, { useState } from 'react';
import ReservationPiker from './ReservationPiker';
import CommonBtn from '../common/CommonBtn/CommonBtn';
import { RoomsProps } from '../../types/ghDetailData';

const GhReservation = ({
  rooms,
  startDay,
  endDay,
  setEndDay,
  setStartDay,
  dayCal,
  setDayCal,
}: RoomsProps) => {
  const [participate, setParticipate] = useState<boolean>(false);
  const [ghPrice, setGhPrice] = useState<number>(0);

  //파티 참석 여부
  const handleParticipate = () => {
    setParticipate(!participate);
  };
  const handleGhPrice = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setGhPrice(Number(e.target.value));
  };
  //예약데이터 리덕스로 관리??
  const ReservationData = () => {
    if (ghPrice === 0) {
      alert('객실을 선택해주세요');
    } else if (dayCal === 0 || (endDay && endDay.length < 6)) {
      alert('예약 일정을 선택해주세요');
    } else console.log('asd');
  };

  return (
    <div className="border-b-[2px] ">
      <div>숙소 예약</div>
      <div className="md:flex md:justify-between flex-row justify-center items-center mb-[20px] ">
        <div className="text-center">
          <ReservationPiker
            startDay={startDay}
            endDay={endDay}
            setDayCal={setDayCal}
            setStartDay={setStartDay}
            setEndDay={setEndDay}
          />
        </div>
        <div className=" md:min-w-[370px] md:m-[0] m-[40px] border-[1px] border-black rounded-xl drop-shadow-2xl bg-white text-lg">
          <form className="mt-[10px] text-center">
            <select
              name="RoomSelect"
              className=" text-center"
              onChange={handleGhPrice}
            >
              <option value={0}>객실 선택</option>
              {rooms
                .filter((el) => el.reservePossible)
                .map((el: any, i: number) => (
                  <option value={el.roomPrice} key={i}>
                    {el.roomName}
                  </option>
                ))}
            </select>
          </form>
          <div className="flex justify-center	items-center mt-[20px] px-[10px] ">
            <div className="rounded-tl-CommentRadius border-y-[1px] border-l-[1px] border-black w-full text-center py-[10px]">
              {startDay}
            </div>
            <div className="rounded-tr-CommentRadius border-black  border-y-[1px] border-x-[1px] w-full text-center  py-[10px]">
              {endDay === '' ? '날짜 추가' : endDay}
            </div>
          </div>
          <div className="flex justify-center	items-center px-[10px]">
            <div className=" w-full flex-row text-start rounded-bl-CommentRadius border-b-[1px] border-l-[1px] border-black  text-center py-[10px]">
              <div className="font-bold ml-[30px]">인원</div>
              <div className="text-base ml-[30px]">게스트 1명</div>
            </div>
            <div className=" flex-row w-full  rounded-br-CommentRadius border-black  border-b-[1px] border-x-[1px] text-center  py-[10px]">
              <div className="text-left font-bold ml-[20px]">
                파티 참석 여부
              </div>
              <div className="flex ml-[20px]">
                <input
                  type="checkbox"
                  className="checked:bg-blue-500 flex"
                  onClick={handleParticipate}
                />
                <div className="text-base">파티에 참석합니다.</div>
              </div>
            </div>
          </div>
          <div className="p-[10px] text-black pl-[20px]">
            <div>총 합계</div>
            <div className="flex justify-between mt-[10px]">
              <div>
                {`₩${ghPrice}`} x {dayCal < 0 ? 0 : dayCal}박
              </div>
              <div className="pr-[20px]">
                ₩
                {ghPrice * dayCal <= 0
                  ? 0
                  : (ghPrice * dayCal).toLocaleString()}
              </div>
            </div>
          </div>
          <div className="my-[10px] px-[10px]">
            <CommonBtn
              text="예약 하기"
              btnSize={'w-full h-[40px]'}
              btnHandler={ReservationData}
            />
          </div>
        </div>
      </div>
    </div>
  );
};
export default GhReservation;
