import React from 'react'
import ShowImagesBtn from './ShowImagesBtn'
import { PiFlowerFill } from 'react-icons/pi'

function PreviousPlantStatus({ previousPlant }) {

  return (
    <div className='col-12'>
      <div key={previousPlant.plant.plantNum} style={{ marginTop: '20px' }}>
        <div style={{ position: 'relative', borderRadius: '10px', backgroundColor: "#FCFCFC", border: '0px solid #212529', padding: '15px' }}>
          <PiFlowerFill style={{ color: '#ffc0cb', position: 'absolute', top: '5px', right: '5px', fontSize: '40px' }}/>
          <p style={{ fontWeight: '600', fontSize: '16px' }} >My {previousPlant.plant.plantNickName}</p>
          <p style={{ fontSize: '16px', color: 'black', opacity: '80%', marginRight: '10px', marginTop: '0px' }} >{previousPlant.plant.plantName}</p>
          <p style={{ fontSize: '16px', color: 'black', opacity: '50%' }}>{previousPlant.plant.plantRegDate.split("T")[0]} ~ {previousPlant.plantHarvestDate.split("T")[0]}</p>
          <div style={{ position: 'absolute', right: '15px', bottom: '15px' }}>
            <ShowImagesBtn plantNickName={previousPlant.plant.plantNickName} plantNum={previousPlant.plant.plantNum} statusType = 'pre' />
          </div>
        </div>
      </div>
    </div>
  )
}

export default PreviousPlantStatus
