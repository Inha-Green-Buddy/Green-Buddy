import axios from "axios";
import { useState } from "react";

export const useFetch = () => {
    const Server_IP = process.env.REACT_APP_Server_IP;

    const [result, setResult] = useState('');

    const getReq = async ({url, data, token}) => {
        const headers = {};
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        try {
            const response = await axios.get(`${Server_IP}/${url}`, { headers });
            setResult(response.data);
        } catch (err) {
            console.log(err);
        }
    }

    const postReq = async ({url, data, token}) => {
        const headers = {};
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        try {
            const response = await axios.post(`${Server_IP}/${url}`, data, { headers }); // 여기서 axios.get을 axios.post로 변경해야 할 것 같습니다.
            setResult(response.data);
        } catch (err) {
            console.log(err);
        }
    }

    return { result, getReq, postReq };
}