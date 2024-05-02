import React from 'react'
import { GrHelp } from 'react-icons/gr';

function HelpBtn({Content, pageName}) {
  return (
    <div>
        <div>
            <button style={{
                backgroundColor: '#73BD72',
                color: "white",
                borderRadius: '50px',
                position: 'fixed',
                width: '40px',
                height: '40px',
                bottom: '30px',
                fontSize: '15px',
                zIndex: '500',
              }} type="button" className="btn" data-bs-toggle="modal" data-bs-target={`#helpBtn${pageName}`} data-bs-whatever="@getbootstrap"><GrHelp color='#FFFFFF' style={{ color: '#FFFFFF' }} /></button>
              <div className="modal fade" id={`helpBtn${pageName}`} tabindex="-1" aria-labelledby="findModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    {/* <div className="modal-content" style={{ height: '500px' }}> */}
                    <div className="modal-content">
                        <div className="modal-header">
                            <h1 className="modal-title fs-5" id="exampleModalLabel">{pageName} help</h1>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <Content></Content>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
  )
}

export default HelpBtn
