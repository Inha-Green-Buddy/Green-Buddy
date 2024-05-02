import React from 'react'
import RegisterPlant from './RegisterPlant'
import KitDelete from './KitDelete'

function NotRegistered({kit, index}) {
  return (
    <div className='col-12'>
      <div key={index}  style={{ marginTop: '20px' }}>
        <div className='col-12' style={{ borderRadius: '10px', backgroundColor:"white", border: '0px solid #212529', padding: '15px' }}>
            {/* <p style={{ fontWeight: '600', fontSize: '16px' }}>Plant is not registered yet</p> */}
          <p style={{ fontWeight: '600', fontSize: '16px', color: '#7E8287' }}>Please register your plant</p>
          {/* <div style={{ width: '100%', display: 'flex', justifyContent: 'center' }}>
          </div> */}
          <p>{kit.kitDeviceName} Kit</p>
          <p>No. {kit.kitNo}</p>
          {/* <p>Serial Number is {kit.kitSerialNum}</p> */}
          <p>Added in {kit.date.split('T')[0]}</p>
          <div style={{ display: 'flex', justifyContent: 'space-between'}}>
            <RegisterPlant kitNo={kit.kitNo}></RegisterPlant>
            <KitDelete kitNo={kit.kitNo}></KitDelete>
          </div>
        </div>
      </div>
    </div>
  )
}

export default NotRegistered
