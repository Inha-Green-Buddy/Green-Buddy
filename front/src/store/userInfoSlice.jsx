import { createAsyncThunk, createSlice, thunkAPI } from '@reduxjs/toolkit'
import Cookies from 'js-cookie';
import axios from 'axios';

const Server_IP = process.env.REACT_APP_Server_IP;

export const fetchUser = createAsyncThunk(
    'userInfo/fetchUser',
    async () => {
        const res = await axios.get(`${Server_IP}/users/me`, {
            headers: {
                "Authorization": `Bearer ${Cookies.get("accessToken")}`
            }
        });
        return res.data
    }
);
    
const userInfoSlice = createSlice({
    name: 'userInfo',
    initialState: {
        value: {
            infos: {
                userNickname: '',
                userId: '',
                userKitList: [],
                userRegDate: '',
            }
        }
    },
    extraReducers: (builder) => {
        builder.addCase(fetchUser.fulfilled, (state, action) => {
            state.value.infos.userId = action.payload.userId;
            state.value.infos.userNickname = action.payload.userNickname;
            state.value.infos.userKitList = action.payload.userKitList;
            state.value.infos.userRegDate = action.payload.userRegDate;
        });
    }
});



export default userInfoSlice;
export const { refreshUserId, refreshUserNickname, refreshUserKitList } = userInfoSlice.actions;
