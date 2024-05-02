import React from 'react'
import axios from 'axios'
import Cookies from 'js-cookie';
import { useState } from 'react';
import { useEffect } from 'react';
import CurrentPlantStatus from './CurrentPlantStatus';
import PreviousPlantStatus from './PreviousPlantStatus';


function PlantStatusContent() {
  const Server_IP = process.env.REACT_APP_Server_IP;
  const [currentPlant, setCurrentPlant] = useState([])
  const [previousPlant, setPreviousPlant] = useState([])

  useEffect(() => {
    receivePlantInfo();
  }, [])

  const receivePlantInfo = async () => {
    await axios.get(`${Server_IP}/kit/plant/list`, {
        headers: {
          "Authorization": `Bearer ${Cookies.get("accessToken")}`
        },
    })
    .then((res) => {
      setCurrentPlant(res.data.plant);
      setPreviousPlant(res.data.previousPlant);
    })
    .catch((err) => {
      console.log(err)
    })
  }
  return (
    <>
      <div className='col-11 col-lg-10' style={{ margin: '0 auto' }}>
        <div style={{ borderBottom: '5px dotted grey', margin: '0 auto', padding: '0', paddingBottom: '20px' }}>
          <div className='row col-12 col-lg-6' style={{ margin: '0 auto', width: '100%' }}>
            {currentPlant.map((plant, index) =>
              <div className="col-12 col-lg-6">
                <CurrentPlantStatus plant={plant}></CurrentPlantStatus>
              </div>
            )}
          </div>
        </div>

        <div style={{ margin: '0 auto', padding: '0' }}>
          <div className='row col-12 col-lg-6' style={{ margin: '0 auto', width: '100%' }}>
            {previousPlant.map((plant, index) =>
              <div className="col-12 col-lg-6">
                <PreviousPlantStatus previousPlant={plant}></PreviousPlantStatus>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  )
}

export default PlantStatusContent