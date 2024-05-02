import React from 'react'
import { useSelector } from 'react-redux';
import NotRegistered from './notRegistered/NotRegistered';
import Registered from './registered/Registered';

function KitsStatus() {

  const userKitList = useSelector(state => state.userInfo.value.infos.userKitList)

  return (
    <div className='row' style={{ width: '100%' }}>
      {userKitList.map((kit, index) =>
        <div className="col-12 col-lg-6">
          {kit.plant ? <Registered kit={kit} index={index} /> : <NotRegistered kit={kit} index={index} />}
        </div>
      )}
    </div>
  )
}

export default KitsStatus