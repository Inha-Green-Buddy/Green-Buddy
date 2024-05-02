import React from 'react'
import ShowImages from './ShowImages'

function ShowImagesBtn({ plantNickName, plantNum }) {
  return (
    <div>
          <button style={{ color: 'white', backgroundColor: '#73BD72', paddingLeft: '7px', paddingRight: '7px' }} type="button" class="btn" data-bs-toggle="modal" data-bs-target={`#carouselModal${plantNum}`}>
              Pictures
          </button>
          <div class="modal fade" id={`carouselModal${plantNum}`} tabindex="-1" aria-labelledby="carouselModalLabel" aria-hidden="true">
              <div class="modal-dialog modal-md">
                  <div class="modal-content">
                      <div class="modal-header">
                          <h5 class="modal-title" id="carouselModalLabel">My {plantNickName}</h5>
                          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                      </div>
                      <div class="modal-body">
                          <ShowImages plantNum={ plantNum } ></ShowImages>
                          <div id="carouselExampleDark" class="carousel carousel-dark slide">
                          </div>
                      </div>
                      <div class="modal-footer">
                          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                      </div>
                  </div>
              </div>
          </div>
    </div>
  )
}

export default ShowImagesBtn
