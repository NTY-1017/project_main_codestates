package main.project.server.guesthouse.service;

import lombok.RequiredArgsConstructor;
import main.project.server.exception.BusinessException;
import main.project.server.exception.ExceptionCode;
import main.project.server.guesthouse.entity.GuestHouse;
import main.project.server.guesthousedetails.repository.GuestHouseDetailsRepository;
import main.project.server.guesthouseimage.entity.GuestHouseImage;
import main.project.server.guesthouse.repository.GuestHouseRepository;
import main.project.server.guesthouseimage.repository.GuestHouseImageRepository;
import main.project.server.utils.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class GuestHouseService {

    @Value("${images.guest-house-dir}")
    private String guestHouseImageDir;
    private final GuestHouseRepository repository;

    private final GuestHouseDetailsRepository guestHouseDetailsRepository;

    private final GuestHouseImageRepository guestHouseImageRepository;

    public GuestHouse createGuestHouse(GuestHouse guestHouse, MultipartFile[] guestHouseImages) throws IOException {

        //entity 저장
        GuestHouse savedGuestHouse = repository.save(guestHouse);

        //파일이 없을 경우에 대한 더 적절한 처리가 필요
        if(guestHouseImages != null && guestHouseImages.length != 0 && !guestHouseImages[0].getOriginalFilename().equals(""))
        {
            //이미지 저장
            List<String> imageUrls = saveFiles(guestHouseImages, savedGuestHouse.getGuestHouseId());
            guestHouse.setGuestHouseImage(urlListToGuestHouseImageList(guestHouse, imageUrls));
        }

        return guestHouse;
    }


    public GuestHouse modifyGuestHouse(GuestHouse guestHouse, MultipartFile[] guestHouseImages) throws IOException {

        //기존 게스트하우스 데이터 가져오기
        GuestHouse existsGuestHouse = verifyExistsGuestHouse(guestHouse.getGuestHouseId());

        //url만 String으로 매핑
        List<String> urlList = existsGuestHouse.getGuestHouseImage().stream().map(
                guestHouseImage -> new String(guestHouseImage.getGuestHouseImageUrl())).collect(Collectors.toList());


        //기존 이미지 파일 삭제, 기존 이미지 데이터 삭제
        guestHouseImageRepository.deleteAllByGuestHouse(guestHouse);
        deleteAllGuestHouseImageByGuestHouse(urlList,guestHouse.getGuestHouseId());


        //파일이 없을 경우에 대한 더 적절한 처리가 필요
        if(guestHouseImages != null && guestHouseImages.length != 0 && !guestHouseImages[0].getOriginalFilename().equals(""))
        {
            //이미지 저장
            List<String> imageUrls = saveFiles(guestHouseImages, guestHouse.getGuestHouseId());
            guestHouse.setGuestHouseImage(urlListToGuestHouseImageList(guestHouse, imageUrls));
        }

        //GuestHouseDetails가 새롭게 생성되지 않도록, DB에서 가져온 GuestHouseDetails의 id를 세팅
        guestHouse.getGuestHouseDetails().setGuestHouseDetailsId(existsGuestHouse.getGuestHouseDetails().getGuestHouseDetailsId());

        //entity 저장
        repository.save(guestHouse);

        return guestHouse;

    }

    public GuestHouse findGuestHouse(Long guestHouseId) {

        return verifyExistsGuestHouse(guestHouseId);
    }


    public GuestHouse verifyExistsGuestHouse(Long guestHouseId) {

        return repository.findById(guestHouseId).orElseThrow(()
                -> {return new BusinessException(ExceptionCode.NOT_EXISTS_GUESTHOUSE);});
    }


    private List<String> saveFiles(MultipartFile[] images, Long guestHouseId) throws IOException {

        String uploadDir = guestHouseImageDir + guestHouseId; //저장 디렉토리 경로
//        Long currentTimeMillis = System.currentTimeMillis();//현재 시간 밀리세컨드로

        List<String> imageUrl = new ArrayList<>();

        for (MultipartFile multipartFile : images) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            FileUtil.saveFile(uploadDir, fileName, multipartFile);

            String totalUrl = uploadDir+ "/" + fileName;
            imageUrl.add(totalUrl);
        }
        return imageUrl;
    }

    private List<String> deleteAllGuestHouseImageByGuestHouse(List<String> imageUrl, Long guestHouseId) throws IOException {

        for (String url : imageUrl) {
            FileUtil.deleteFile(url);
        }

        return imageUrl;
    }

    private List<GuestHouseImage> urlListToGuestHouseImageList(GuestHouse guestHouse,List<String> urls) {

        return urls.stream().map(url ->
                GuestHouseImage.builder().guestHouse(guestHouse).guestHouseImageUrl(url).build()).collect(Collectors.toList());
    }

}