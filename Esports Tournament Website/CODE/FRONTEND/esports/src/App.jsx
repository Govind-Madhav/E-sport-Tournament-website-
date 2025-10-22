import './index.css'
import { BrowserRouter as Router,Routes,Route } from 'react-router-dom';
import Homepage from './Components/Homepage';
import AdminPage from './Components/Admin/AdminPage';
import ManageUsers from './Components/Admin/ManageUsers';
import ManageHosts from './Components/Admin/ManageHosts';
import ManageTournments from './Components/Admin/ManageTournments';
import ManageTransactions from './Components/Admin/ManageTransactions';
import HostHomepage from './Components/Host/HostHomepage';
import HostmanageTourn from './Components/Host/HostmanageTourn';
import HostManageUser from './Components/Host/HostManageUser';
import HostUserPayments from './Components/Host/HostUserPayments';
import ViewTourn from './Components/User/ViewTourn';
import UserHomePage from './Components/User/UserHomePage';
import UserPaidTourn from './Components/User/UserPaidTourn';
import Leaderboard from './Components/User/Leaderboard';
import Profile from './Components/User/Profile';
import HostDeclareWinners from './Components/Host/HostDeclareWinners';


function App() {

  return (
      <Router>
        <Routes>
          <Route path='/' element={<Homepage/>}/>
          <Route path='/adminPage' element={<AdminPage/>} />
          <Route path='/manageUsers' element={<ManageUsers/>}/>
          <Route path='/manageHosts' element={<ManageHosts/>}/>
          <Route path='/manageTourn' element={<ManageTournments/>}/>
          <Route path='/manageTrans' element={<ManageTransactions/>}/>
          <Route path='/hostPage' element={<HostHomepage/>}/>
          <Route path='/hostTourn' element={<HostmanageTourn/>} />
          <Route path='/hostUsers' element={<HostManageUser/>}/>
          <Route path='/hostUsersPayment' element={<HostUserPayments/>}/>
          <Route path='/viewTourn' element={<ViewTourn/>}/>
          <Route path='/userHomePage' element={<UserHomePage/>}/>
          <Route path='/userPaidTourn' element={<UserPaidTourn/>}/>
          <Route path='/leaderboard' element={<Leaderboard/>}/>
          <Route path='/profile' element={<Profile/>}/>
          <Route path='declare-winners' element={<HostDeclareWinners/>}/>
        </Routes>
      </Router>
  )
}

export default App
