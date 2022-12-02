import { useEffect, useState } from 'react';

import MyPageTabs from '../components/MyPage/MyPageTabs/MyPageTabs';
import UserInfoCard from '../components/MyPage/UserInfoCard/UserInfoCard';
import { User1, User2 } from '../types/user';

import { getUser } from '../api2/member';
import { convertURLtoFile } from '../libs/srcToFile';

function MyPage() {
  const [user, setUser] = useState<User1>({
    memberId: '',
    memberBirth: '',
    memberEmail: '',
    memberImageFile: [],
    memberNationality: '',
    memberNickname: '',
    memberPhone: '',
    memberRegisterKind: '',
    memberRoles: [],
    memberTag: [],
  });
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    const getGhdata = async () => {
      // 유저 정보 가져 오기
      const userGet = (await getUser()) as User2;
      const FileData = await convertURLtoFile(
        `${process.env.REACT_APP_SERVER_URL}${userGet.memberImageUrl}`
      );

      setUser({
        ...userGet,
        memberImageFile: [FileData],
      });
      setLoading(true);
    };
    getGhdata();
  }, []);

  return (
    <div className="flex justify-between w-full h-full py-[20px]">
      {loading && (
        <>
          <UserInfoCard user={user} setUser={setUser} />
          <MyPageTabs />
        </>
      )}
    </div>
  );
}

export default MyPage;
