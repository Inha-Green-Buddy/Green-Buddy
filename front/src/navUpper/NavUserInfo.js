import React from 'react'
import { useCookies } from 'react-cookie';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import ChangePassword from './ChangePassword';
import { CiLogout } from 'react-icons/ci'
import { PiPottedPlantFill } from 'react-icons/pi'
import { CgUiKit } from 'react-icons/cg'
import { SiLeaflet } from 'react-icons/si'
import './navUserInfo.css';
import ChangeNickname from './ChangeNickname';

function NavUserInfo() {
  const [cookies, setCookie, removeCookie] = useCookies(['accessToken']);
  const userNickname = useSelector(state => state.userInfo.value.infos.userNickname)
  const userKitList = useSelector(state => state.userInfo.value.infos.userKitList)
  const userRegDate = useSelector(state => state.userInfo.value.infos.userRegDate)

  const countGrowingPlant = () => {
    let cnt = 0;
    userKitList.map((lst) => {
      if (lst.plant) {
        cnt += 1;
      }
    })
    return cnt
  }

  let growingPlantCnt = countGrowingPlant();

  const navigate = useNavigate();

  function formatDateToEnglishStyle(dateString) {
    const months = [
      'January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November', 'December'
    ];

    const [year, month, day] = dateString.split('-');

    const monthName = months[parseInt(month) - 1];

    return `${monthName} ${day}, ${year}`;
  }


  return (
    <>
      <div style={{ width: '300px' }} className="offcanvas offcanvas-end" tabindex="-1" id="offcanvasRight" aria-labelledby="offcanvasRightLabel" >
        <div className="offcanvas-header" style={{ display: 'flex', justifyContent: 'right' }}>
          <button type="button" className="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
        </div>
        <div className="offcanvas-body">
          <div>
            <div style={{ fontSize: '30px' }}>
              {userNickname ? `Welcome ${userNickname}!` : 'Please try to login'}
            </div>
            <div style={{
              fontSize: '16px', marginTop: '30px', color: 'grey', paddingTop: '10px' }}>
              <SiLeaflet /> Growing since {formatDateToEnglishStyle(userRegDate.split("T")[0])}
            </div>
            <div style={{ fontSize: '16px', marginTop: '10px', color: 'grey' }}>
              <CgUiKit /> You have a total of {userKitList.length} kits
            </div>
            <div style={{ fontSize: '16px', marginTop: '10px', color: 'grey' }}>
              <PiPottedPlantFill /> You're with {growingPlantCnt} plants
            </div>
          </div>
          <div style={{ marginTop: '30px', marginBottom: '30px', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'flex-start', borderTop: '2px #7AA278 solid', paddingTop: '20px'  }}>
            <ChangeNickname exUserNickname={userNickname}></ChangeNickname>
            <ChangePassword></ChangePassword>
          </div>
          <div style={{
            borderTop: '2px #7AA278 solid', marginTop: '10px' }}>
            <button type="button" className='btn hover-button' style={
              {
              color: 'black',
              borderRadius: '0',
              paddingLeft: '7px',
              paddingRight: '7px',
              width: '100%',
              textAlign: 'left',
              fontSize: '18px',
              fontWeight: 'bold'
              }} onClick={() => {
              removeCookie("accessToken")
              navigate("/login");
              window.location.reload();
              }}> <CiLogout/> Logout</button>
          </div>
        </div>
      </div>
    </>

  )
}

export default NavUserInfo