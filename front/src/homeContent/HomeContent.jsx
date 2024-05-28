import React, { useEffect, useState } from 'react'
import AddDevice from './AddDevice';
import KitsStatus from './kitsStatus/KitsStatus';
import { useFetch } from '../hooks/useFetch';
import { useAccessToken } from '../contexts/AccessTokenContext';

function HomeContent() {

    const { statusCode: testStatusCode, postReq: testPost, response: testResponse } = useFetch();
    const { accessToken, setAccessToken } = useAccessToken();

    const testRequest = async () => {
        await testPost({
            url: 'auth/reissue',
            data: { accessToken: accessToken },
            token: false,
        })
    }

    useEffect(() => {
        if (testStatusCode) {
            console.log(testResponse);
            console.log(testStatusCode);
        }
    }, [testStatusCode])

    return(
        <div>
            <div style={{ display:'flex', alignItems: 'center' }}>
                <div className='col-11 col-lg-10' style={{ justifyContent:'right', margin: '0 auto' }}>
                    <AddDevice></AddDevice>
                </div>
            </div>

            <div className='col-11 col-lg-10' style={{ margin: '0 auto', display: 'flex', flexDirection: 'column'}}>
                <div style={{ display: 'flex', justifyContent: 'center', margin: '0', padding: '0' }}>
                    <KitsStatus/>
                </div>
            </div>
        </div>
    )

}

export default HomeContent
