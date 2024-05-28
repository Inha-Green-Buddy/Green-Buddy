import axios from "axios";
import { useState } from "react";
import { useAccessToken } from "../contexts/AccessTokenContext";

export const useFetch = () => {
    const Server_IP = process.env.REACT_APP_Server_IP;

    const [response, setResponse] = useState('');
    const [statusCode, setStatusCode] = useState('');
    const { accessToken, setAccessToken } = useAccessToken();

    const reissueAccessToken = async () => {
        await axios.get(`${Server_IP}/auth/reissue`, {
            accessToken: accessToken
        })
        .then((res) => {
            setAccessToken(res.data.accessToken)
        });
    }

    const getReq = async ({url, data, token, cookies}) => {
        const headers = {};
        if (token) {
            headers['Authorization'] = `Bearer ${accessToken}`;
        }
        try {
            const response = await axios.get(`${Server_IP}/${url}`, { headers });
            setResponse(response.data);
            setStatusCode(response.status);
        } catch (err) {
            if (err.response.status === 403) {
                await reissueAccessToken();
                await getReq({ url, data, token, cookies });
            }
            console.log(err);
            setStatusCode(err.response.status);
        }
    }

    const postReq = async ({url, data, token, cookies}) => {
        const header = { "Content-Type": "application/json" };
        if (token) {
            header['Authorization'] = `Bearer ${accessToken}`;
        }
        try {
            const response = await axios.post(`${Server_IP}/${url}`,
                data,
                { headers: header },
                cookies ? { withCredentials: true } : {},
            );
            console.log(response)
            setResponse(response.data);
            setStatusCode(response.status);
        } catch (err) {
            if (err.response.status === 403) {
                await reissueAccessToken();
                await postReq({ url, data, token, cookies });
            }
            setStatusCode(err.response.status);
            console.log(err);
        }
    }

    const deleteReq = async ({url, data, token, cookies}) => {
        const header = { "Content-Type": "application/json" };
        if (token) {
            header['Authorization'] = `Bearer ${accessToken}`;
        }
        try {
            const response = await axios.delete(`${Server_IP}/${url}`,
                data,
                { headers: header },
                cookies ? { withCredentials: true } : {},
            );
            console.log(response)
            setResponse(response.data);
            setStatusCode(response.status);
        } catch (err) {
            if (err.response.status === 403) {
                await reissueAccessToken();
                await postReq({ url, data, token, cookies });
            }
            setStatusCode(err.response.status);
            console.log(err);
        }
    }

    return { statusCode, response, getReq, postReq, deleteReq, setStatusCode };
}