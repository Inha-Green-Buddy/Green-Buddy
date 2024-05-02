import React from 'react'
import FindPlant from './FindPlant'

function SearchContent() {
  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'center' }}>
        <div className='col-11 col-lg-10'>
          <div style={{ margin: '6px 12px', backgroundColor: 'white', borderRadius: '10px' }}>
            <FindPlant></FindPlant>
          </div>
        </div>
      </div>
    </div>
  )
}

export default SearchContent