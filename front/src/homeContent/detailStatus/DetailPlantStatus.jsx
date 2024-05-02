import React from 'react'
import ThisPlantInfo from './ThisPlantInfo';
import { useState, useEffect } from 'react';
import axios from 'axios';

function DetailPlantStatus({ kit }) {

    useEffect(() => {
        receivePlantDetail();
    }, []);

    const [plantDetail, setPlantDetail] = useState({});
    const receivePlantDetail = async () => {
        try {
            const res = await axios.get('/plantInfoData.json');
            setPlantDetail(res.data[kit.plant.plantName]);
        } catch (err) {
            console.error("Error fetching plant data:", err);
        }
    }

    const dateDifference = (givenDate) => {
        const formattedDate = givenDate.split(' ')[0]; // "2023-08-09"
        const date1 = new Date(formattedDate);
        const date2 = new Date(); // 현재 날짜

        // 두 날짜의 차이를 밀리초 단위로 계산
        const differenceInTime = date2 - date1;

        // 밀리초를 일로 변환
        const differenceInDays = differenceInTime / (1000 * 3600 * 24);

        return (Math.floor(differenceInDays) + 1)
    }

    return (
        <div>
            <div style={{ padding: '10px', lineHeight: '2.5', display:'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
                <div className='plantInfo' >
                    <div>
                        Date to start : {kit.plant.plantRegDate.split('T')[0].replace(/-/g, ' / ')}
                        <span> {`(${dateDifference(kit.plant.plantRegDate)} Days)`}</span>
                    </div>
                </div>
                <div className='plantDetailInfo'>
                    <div><ThisPlantInfo plantDetail={plantDetail}></ThisPlantInfo></div>
                </div>
            </div>
        </div>
    )
}

export default DetailPlantStatus
