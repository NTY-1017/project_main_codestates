package main.project.server.guesthouse.mapper;


import main.project.server.city.entity.City;
import main.project.server.guesthouse.dto.GuestHouseDto;
import main.project.server.guesthouse.entity.GuestHouse;
import main.project.server.guesthouse.entity.enums.GuestHouseStatus;
import main.project.server.guesthousedetails.entity.GuestHouseDetails;

import main.project.server.guesthousedetails.mapper.GuestHouseDetailsMapper;
import main.project.server.member.entity.Member;
import main.project.server.review.dto.ReviewDto;
import main.project.server.room.mapper.RoomMapper;
import main.project.server.room.service.RoomService;

import org.mapstruct.Mapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface GuestHouseMapper {

    default public GuestHouse guestHouseDtoPostToGuestHouse(GuestHouseDto.Post dto, String memberId){


        GuestHouse guestHouse = GuestHouse.builder()
                .guestHouseName(dto.getGuestHouseName())
                .member(Member.Member(memberId))
                .city(City.City(dto.getCityId()))
                .guestHouseLocation(dto.getGuestHouseLocation())
                .guestHouseAddress(dto.getGuestHouseAddress())
                .guestHousePhone(dto.getGuestHousePhone())
                .guestHouseStatus(GuestHouseStatus.OPEN)
                .guestHouseDetails(booleanArrayToGuestHouseDetails(dto.getGuestHouseDetails()))
                .guestHouseTag(createSortedTagString(dto.getGuestHouseTag()))
                .guestHouseInfo(dto.getGuestHouseInfo())
                .guestHouseReviewCount(0L) //최초 등록시 초기화
                .guestHouseStar(0f) //최초 등록시 초기화
                .build();

        guestHouse.getGuestHouseDetails().setGuestHouse(guestHouse);

        return guestHouse;

    }


    default public GuestHouse guestHouseDtoPutToGuestHouse(GuestHouseDto.Put dto, String memberId){

        GuestHouse guestHouse = GuestHouse.builder()
                .guestHouseName(dto.getGuestHouseName())
                .member(Member.Member(memberId))
                .city(City.City(dto.getCityId()))
                .guestHouseLocation(dto.getGuestHouseLocation())
                .guestHouseAddress(dto.getGuestHouseAddress())
                .guestHousePhone(dto.getGuestHousePhone())
                .guestHouseStatus(GuestHouseStatus.OPEN)
                .guestHouseDetails(booleanArrayToGuestHouseDetails(dto.getGuestHouseDetails()))
                .guestHouseTag(createSortedTagString(dto.getGuestHouseTag()))
                .guestHouseInfo(dto.getGuestHouseInfo())
                .build();

        guestHouse.getGuestHouseDetails().setGuestHouse(guestHouse);

        return guestHouse;
    }



    default List<GuestHouseDto.response> guestHouseListToGuestHouseResponse(
            List<GuestHouse> guestHouseList,
            RoomService roomService,
            GuestHouseDetailsMapper guestHouseDetailsMapper) {

        return guestHouseList.stream().map(guestHouse -> {

            return GuestHouseDto.response.builder()
                    .guestHouseId(guestHouse.getGuestHouseId())
                    .guestHouseName(guestHouse.getGuestHouseName())
                    .memberId(guestHouse.getMember().getMemberId())
                    .memberNickname(guestHouse.getMember().getMemberNickname())
                    .memberPhone(guestHouse.getMember().getMemberPhone())
                    .memberImageUrl(guestHouse.getMember().getMemberImageUrl())
                    .guestHouseLocation(guestHouse.getGuestHouseLocation())
                    .guestHouseAddress(guestHouse.getGuestHouseAddress())
                    .guestHousePhone(guestHouse.getGuestHousePhone())
                    .guestHouseStatus(guestHouse.getGuestHouseStatus())
                    .guestHouseDetails(guestHouseDetailsToBooleanArray(guestHouse.getGuestHouseDetails()))
                    .guestHouseStar(guestHouse.getGuestHouseStar())
                    .guestHouseTag(createSortedTagArray(guestHouse.getGuestHouseTag()))
                    .guestHouseImage(guestHouse.guestHouseImageListToUrlList())
                    .guestHouseInfo(guestHouse.getGuestHouseInfo())
                    .guestHouseReviewCount(guestHouse.getGuestHouseReviewCount())
                    .createdAt(guestHouse.getCreatedAt().toString())
                    .modifiedAt(guestHouse.getModifiedAt().toString())
                    .build();

        }).collect(Collectors.toList());
    }

    default GuestHouseDetails booleanArrayToGuestHouseDetails(Boolean[] option) {

        return GuestHouseDetails.builder()
                .guestHouseParty(option[0])
                .guestHouseKitchen(option[1])
                .guestHouseWashing(option[2])
                .guestHouseOcean(option[3])
                .guestHouseTask(option[4])
                .guestHouseEssential(option[5])
                .guestHouseWifi(option[6])
                .guestHouseBoard(option[7])
                .guestHouseCook(option[8])
                .guestHouseParking(option[9])
                .build();
    }




    /** 배열로 넘어온 태그를 정렬하여 DB에 저장되는 포맷의 형태로 변환시켜 주는 메소드 **/
    default String createSortedTagString(String[] tags) {

        Arrays.sort(tags);

        StringBuilder stringBuilder = new StringBuilder();
        for (String tag : tags) {

            stringBuilder.append("|" + tag + "|");
        }
        return stringBuilder.toString();
    }

    default String[] createSortedTagArray(String tags) {

        String[] splStr;

        if(tags == null) //태그가 없는 경우
            return new String[0];

        if (tags.contains("||")) { //2개 이상 경우

            splStr = tags.split("\\|\\|");
            // |dd, ddd|, ...
        }
        else { //1개 경우

            splStr = new String[]{tags};
            // |dd|
        }

        String[] tagArray = Arrays.stream(splStr).map(str ->
                new String(str.replace("|", ""))).toArray(str -> new String[str]);

        return tagArray;
    }


    default GuestHouseDto.response guestHouseToSingleGuestHouseResponse(

            GuestHouse guestHouse,
            RoomService roomService,
            String start,
            String end,
            List<ReviewDto.Response> reviews) {

        Member adminMember = guestHouse.getMember();

        return GuestHouseDto.response.builder()
                .guestHouseId(guestHouse.getGuestHouseId())
                .guestHouseName(guestHouse.getGuestHouseName())
                .memberId(adminMember.getMemberId())
                .memberNickname(adminMember.getMemberNickname())
                .memberPhone(adminMember.getMemberPhone())
                .memberImageUrl(adminMember.getMemberImageUrl())
                .guestHouseLocation(guestHouse.getGuestHouseLocation())
                .guestHouseAddress(guestHouse.getGuestHouseAddress())
                .guestHousePhone(guestHouse.getGuestHousePhone())
                .guestHouseStatus(guestHouse.getGuestHouseStatus())
                .guestHouseDetails(guestHouseDetailsToBooleanArray(guestHouse.getGuestHouseDetails()))
                .guestHouseStar(guestHouse.getGuestHouseStar())
                .guestHouseTag(createSortedTagArray(guestHouse.getGuestHouseTag()))
                .guestHouseImage(guestHouse.guestHouseImageListToUrlList()) //리스트, 처리 필요
                .guestHouseInfo(guestHouse.getGuestHouseInfo())
                .rooms(roomService.getReservePossibleOfRoomDtoResponseList(guestHouse.getGuestHouseId(), guestHouse.getRooms(), start, end)) //리스트, 처리 필요
                .guestHouseReviewCount(guestHouse.getGuestHouseReviewCount())
                .reviews(reviews)   // 리뷰
                .createdAt(guestHouse.getCreatedAt().toString())
                .modifiedAt(guestHouse.getModifiedAt().toString())
                .build();
    }

    default Boolean[] guestHouseDetailsToBooleanArray(GuestHouseDetails guestHouseDetails) {

        return new Boolean[]{
                guestHouseDetails.getGuestHouseParty(),
                guestHouseDetails.getGuestHouseWashing(),
                guestHouseDetails.getGuestHouseOcean(),
                guestHouseDetails.getGuestHouseTask(),
                guestHouseDetails.getGuestHouseEssential(),
                guestHouseDetails.getGuestHouseWifi(),
                guestHouseDetails.getGuestHouseBoard(),
                guestHouseDetails.getGuestHouseCook(),
                guestHouseDetails.getGuestHouseParking()
        };
    }



}