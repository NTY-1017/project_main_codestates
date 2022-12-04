import Api from './index';

export const getUser = async () => {
  try {
    const response = await Api.get(`/api/auth/members/`);
    return response.data.data;
  } catch (e) {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }
};
