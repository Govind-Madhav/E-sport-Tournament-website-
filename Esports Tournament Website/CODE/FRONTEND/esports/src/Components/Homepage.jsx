import React, { useRef, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '@fontsource/poppins';
import { motion } from 'framer-motion';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { 
  FaTrophy, FaUsers, FaSun, FaMoon, 
  FaFootballBall, FaTwitter, 
  FaInstagram, FaFacebookF, FaYoutube,
  FaCalendarAlt, FaDollarSign, FaClock, FaGamepad
} from 'react-icons/fa';

const HomePage = () => {
  const aboutRef = useRef(null);
  const navigate = useNavigate();

  const [darkMode, setDarkMode] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const [showForgotModal, setShowForgotModal] = useState(false); // 🆕
  
  const [selectedRole, setSelectedRole] = useState('');
  const [loginEmail, setLoginEmail] = useState('');
  const [loginPassword, setLoginPassword] = useState('');
  
  const [registerFullName, setRegisterFullName] = useState('');
  const [registerEmail, setRegisterEmail] = useState('');
  const [registerPassword, setRegisterPassword] = useState('');
  const [registerMobile, setRegisterMobile] = useState('');
  const [registerFile, setRegisterFile] = useState(null);

  const [forgotEmail, setForgotEmail] = useState(''); // 🆕
  const [forgotNewPassword, setForgotNewPassword] = useState(''); // 🆕
  const [forgotConfirmPassword, setForgotConfirmPassword] = useState(''); // 🆕
  
  const [loading, setLoading] = useState(false);
  
  // 🆕 Tournament data states
  const [tournaments, setTournaments] = useState({
    ongoing: [],
    upcoming: []
  });
  const [tournamentsLoading, setTournamentsLoading] = useState(true);
  const [tournamentsError, setTournamentsError] = useState(null);

  const handleScrollToAbout = () => {
    if (aboutRef.current) {
      aboutRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  };

  // 🆕 Fetch tournament data for homepage
  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        setTournamentsLoading(true);
        setTournamentsError(null);
        
        // Try to fetch from backend first
        try {
          const response = await axios.get('http://localhost:8080/api/player/homepage-tournaments');
          
          if (response.data.status === 'success') {
            setTournaments(response.data.data);
          } else {
            setTournamentsError('Failed to fetch tournaments');
          }
        } catch (backendError) {
          // If backend is not available, use mock data for demonstration
          console.log('Backend not available, using mock data for demonstration');
          setTournaments({
            ongoing: [
              {
                id: 1,
                name: "CS:GO Championship 2024",
                description: "The ultimate Counter-Strike tournament with the best teams competing for glory and prizes.",
                startDate: "2024-01-15",
                endDate: "2024-01-20",
                gameType: "Counter-Strike",
                joiningFee: 50,
                imageUrl: "/img1.png"
              }
            ],
            upcoming: [
              {
                id: 2,
                name: "Valorant Masters",
                description: "Join the biggest Valorant tournament of the year with teams from around the world.",
                startDate: "2024-02-01",
                endDate: "2024-02-05",
                gameType: "Valorant",
                joiningFee: 75,
                imageUrl: "/img2.png"
              },
              {
                id: 3,
                name: "Fortnite Battle Royale",
                description: "Epic Fortnite tournament with amazing prizes and competitive gameplay.",
                startDate: "2024-02-10",
                endDate: "2024-02-12",
                gameType: "Fortnite",
                joiningFee: 25,
                imageUrl: "/img3.png"
              }
            ]
          });
        }
      } catch (error) {
        console.error('Error fetching tournaments:', error);
        setTournamentsError('Failed to load tournaments. Please try again later.');
        // Set empty data as fallback
        setTournaments({ ongoing: [], upcoming: [] });
      } finally {
        setTournamentsLoading(false);
      }
    };

    fetchTournaments();
  }, []);

  // 🆕 Helper function to format date
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  // 🆕 Helper function to get registration status
  const getRegistrationStatus = (tournament) => {
    const now = new Date();
    const startDate = new Date(tournament.startDate);
    const endDate = new Date(tournament.endDate);
    
    if (now < startDate) {
      return { status: 'upcoming', text: 'Registration Open', color: 'text-green-500' };
    } else if (now >= startDate && now <= endDate) {
      return { status: 'ongoing', text: 'Live Now', color: 'text-red-500' };
    } else {
      return { status: 'ended', text: 'Registration Closed', color: 'text-gray-500' };
    }
  };

  const roles = [
    { label: 'Player', value: 'player' },
    { label: 'Host', value: 'host' }
  ];

  const aroles = [
    { label: 'Admin', value: 'admin' },
    { label: 'Player', value: 'player' },
    { label: 'Host', value: 'host' }
  ];


  // 🆕 Improved login function with automatic role detection
  const handleLoginSubmit = async (e) => {
    e.preventDefault();

    const loginData = {
        email: loginEmail,
        password: loginPassword
    };

    try {
        setLoading(true);
        
        // Try unified login first (for players and hosts)
        let response;
        try {
            response = await axios.post("http://localhost:8080/api/player/unified-login", loginData);
        } catch (unifiedError) {
            // If unified login fails, try admin login
            if (loginEmail === "admin@gmail.com") {
                response = await axios.post("http://localhost:8080/api/player/admin-login", loginData);
            } else {
                throw unifiedError;
            }
        }

        // Check if the login is successful and save data in sessionStorage
        if (response.data?.data) {
          const userData = response.data.data;
          const userRole = response.data.role;
          const redirectPath = response.data.redirectPath;
        
          // Store user data in sessionStorage
          sessionStorage.setItem('userId', userData.id);
          sessionStorage.setItem('userName', userData.fullName);
          sessionStorage.setItem('userEmail', userData.email);
          sessionStorage.setItem('userRole', userRole);
          sessionStorage.setItem('userVerified', userData.verified);
          
          if (userData.mobile) {
            sessionStorage.setItem('userMobile', userData.mobile);
          }
          if (userData.imageUrl) {
            sessionStorage.setItem('userImageUrl', userData.imageUrl);
          }
        
          // Store role-specific data for backward compatibility
          if (userRole === 'PLAYER') {
            sessionStorage.setItem('playerId', userData.id);
            sessionStorage.setItem('playerName', userData.fullName);
            sessionStorage.setItem('playerEmail', userData.email);
            sessionStorage.setItem('playerRole', userRole);
            sessionStorage.setItem('playerVerified', userData.verified);
            if (userData.mobile) sessionStorage.setItem('playerMobile', userData.mobile);
            if (userData.imageUrl) sessionStorage.setItem('playerImageUrl', userData.imageUrl);
          } else if (userRole === 'HOST') {
            sessionStorage.setItem('hostId', userData.id);
            sessionStorage.setItem('hostName', userData.fullName);
            sessionStorage.setItem('hostEmail', userData.email);
            sessionStorage.setItem('hostRole', userRole);
            sessionStorage.setItem('hostVerified', userData.verified);
            if (userData.mobile) sessionStorage.setItem('hostMobile', userData.mobile);
            if (userData.imageUrl) sessionStorage.setItem('hostImageUrl', userData.imageUrl);
          }
        
          console.log(`${userRole} data stored in sessionStorage:`, userData);
        }
        
        toast.success(`Login successful as ${response.data.role}!`);

        // Auto-redirect based on role
        setTimeout(() => {
            navigate(response.data.redirectPath);
        }, 1500);

        setLoginEmail('');
        setLoginPassword('');

    } catch (error) {
        console.error("Login Failed:", error.response?.data || error.message);
        const errorMessage = error.response?.data?.message || "Login failed! Please check your credentials.";
        toast.error(errorMessage);
    } finally {
        setLoading(false);
    }
};

  
  

  // 🆕 Register Submit Function
  const handleRegisterSubmit = async (e) => {
    e.preventDefault();

    if (!selectedRole) {
      toast.warn("Please select a role before registering!");
      return;
    }
    if (!registerFile) {
      toast.warn("Please upload a file (image)!");
      return;
    }

    const formData = new FormData();
    formData.append('fullName', registerFullName);
    formData.append('email', registerEmail);
    formData.append('password', registerPassword);
    formData.append('mobile', registerMobile);
    formData.append('file', registerFile);

    let registerUrl = "";

    if (selectedRole === 'host') {
      registerUrl = "http://localhost:8080/api/host/register";
    } else if (selectedRole === 'player') {
      registerUrl = "http://localhost:8080/api/player/register";
    } else {
      toast.error("Only Host or Player registration is allowed!");
      return;
    }

    try {
      setLoading(true);
      const response = await axios.post(registerUrl, formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });

      console.log("Registration successful", response.data);
      toast.success(`Registered successfully as ${selectedRole}! Awaiting approval.`);

      setShowModal(false);
      setRegisterFullName('');
      setRegisterEmail('');
      setRegisterPassword('');
      setRegisterMobile('');
      setRegisterFile(null);
      setSelectedRole('');

    } catch (error) {
      console.error("Registration Failed:", error.response?.data || error.message);
      toast.error("Registration failed! Please try again.");
    } finally {
      setLoading(false);
    }
  };

  // 🆕 Forgot Password Submit Function
  const handleForgotPasswordSubmit = async (e) => {
    e.preventDefault();

    if (!selectedRole) {
      toast.warn("Please select a role for forgot password!");
      return;
    }

    if (forgotNewPassword !== forgotConfirmPassword) {
      toast.warn("New password and Confirm password do not match!");
      return;
    }

    const forgotData = {
      email: forgotEmail,
      newPassword: forgotNewPassword,
      confirmPassword: forgotConfirmPassword
    };

    let forgotUrl = "";

    if (selectedRole === 'host') {
      forgotUrl = "http://localhost:8080/api/host/forgot-password";
    } else if (selectedRole === 'player') {
      forgotUrl = "http://localhost:8080/api/player/forgot-password";
    } else {
      toast.error("Forgot password only allowed for Host or Player!");
      return;
    }

    try {
      setLoading(true);
      const response = await axios.put(forgotUrl, forgotData);

      console.log("Password Reset Successful", response.data);
      toast.success("Password reset successfully!");

      setShowForgotModal(false);
      setForgotEmail('');
      setForgotNewPassword('');
      setForgotConfirmPassword('');
      setSelectedRole('');

    } catch (error) {
      console.error("Password Reset Failed:", error.response?.data || error.message);
      toast.error("Password reset failed! Please try again.");
    } finally {
      setLoading(false);
    }
  };


  return (
    <div className={`${darkMode ? 'bg-gradient-to-br from-[#0a0a0a] via-[#111827] to-[#1f2937]' : 'bg-gradient-to-br from-gray-50 to-white'} font-poppins min-h-screen flex flex-col transition-colors duration-300`}>
      <ToastContainer position="top-center" autoClose={3000} />

      {/* 🌐 Enhanced Navbar */}
      <nav className={`${darkMode ? 'bg-black/40 backdrop-blur-xl border-b border-gray-700' : 'bg-white/90 backdrop-blur-xl border-b border-gray-200'} shadow-md py-4 px-8 flex justify-between items-center`}>
        <div className="flex items-center space-x-3">
          <FaFootballBall className={`text-3xl ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'}`} />
          <h1 className={`text-2xl font-bold ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'}`}>TITAN ESPORTS ⚡</h1>
        </div>
        <div className="space-x-6 flex items-center">
          <button onClick={() => setShowModal(true)} className={`transition ${darkMode ? 'text-gray-300 hover:text-[#00FFFF]' : 'text-gray-700 hover:text-blue-600'}`}>Register</button>
          <button onClick={() => setShowLoginModal(true)} className={`transition ${darkMode ? 'text-gray-300 hover:text-[#00FFFF]' : 'text-gray-700 hover:text-blue-600'}`}>Login</button>
          <button onClick={handleScrollToAbout} className={`transition ${darkMode ? 'text-gray-300 hover:text-[#00FFFF]' : 'text-gray-700 hover:text-blue-600'}`}>About</button>
          <button onClick={() => setDarkMode(!darkMode)} className="ml-4 p-2 rounded-full border border-gray-400 hover:border-[#00FFFF] transition">
            {darkMode ? <FaSun className="text-yellow-300" /> : <FaMoon className="text-gray-800" />}
          </button>
        </div>
      </nav>

      {/* 🔐 Register Modal */}
    {showModal && (
      <div className="fixed inset-0 bg-black bg-opacity-70 flex justify-center items-center z-50">
        <motion.div 
          initial={{ opacity: 0, scale: 0.9 }} 
          animate={{ opacity: 1, scale: 1 }} 
          transition={{ duration: 0.3 }} 
          className={`rounded-xl p-8 w-full max-w-md ${darkMode ? 'bg-gray-900 text-white' : 'bg-white text-black'} shadow-2xl relative`}
        >
          <button onClick={() => setShowModal(false)} className="absolute top-2 right-4 text-2xl font-bold">×</button>
          <h2 className="text-2xl font-bold text-center mb-6">Create Your Account</h2>

          <form className="space-y-4" onSubmit={handleRegisterSubmit}>
            <input 
              type="file" 
              className="w-full border rounded p-2" 
              accept="image/*" 
              onChange={(e) => setRegisterFile(e.target.files[0])}
              required
            />
            <input 
              type="text" 
              placeholder="Full Name" 
              className="w-full border rounded p-2 text-black" 
              value={registerFullName}
              onChange={(e) => setRegisterFullName(e.target.value)}
              required
            />
            <input 
              type="email" 
              placeholder="Email" 
              className="w-full border rounded p-2 text-black" 
              value={registerEmail}
              onChange={(e) => setRegisterEmail(e.target.value)}
              required
            />
            <input 
              type="password" 
              placeholder="Password" 
              className="w-full border rounded p-2 text-black" 
              value={registerPassword}
              onChange={(e) => setRegisterPassword(e.target.value)}
              required
            />
            <input 
              type="tel" 
              placeholder="Mobile Number" 
              className="w-full border rounded p-2 text-black" 
              value={registerMobile}
              onChange={(e) => setRegisterMobile(e.target.value)}
              required
            />
            <div className="flex justify-between gap-2">
              {roles.map((role) => (
                <button
                  key={role.value}
                  type="button"
                  onClick={() => setSelectedRole(role.value)}
                  className={`flex-1 py-2 rounded-lg font-semibold border transition ${selectedRole === role.value ? 'bg-yellow-400 text-black' : `${darkMode ? 'bg-gray-700 text-white' : 'bg-gray-100 text-gray-800'}`}`}
                >
                  {role.label}
                </button>
              ))}
            </div>

            <button 
              type="submit" 
              className={`w-full py-2 rounded font-semibold flex items-center justify-center ${darkMode ? 'bg-yellow-400 text-black hover:bg-yellow-500' : 'bg-yellow-600 text-white hover:bg-yellow-700'}`}
              disabled={loading}
            >
              {loading ? (
                <svg className="animate-spin h-5 w-5 mr-3 border-t-2 border-b-2 border-gray-900 rounded-full" viewBox="0 0 24 24"></svg>
              ) : (
                'Register'
              )}
            </button>
          </form>
        </motion.div>
      </div>
    )}


    {/* 🔐 Enhanced Login Modal */}
    {showLoginModal && (
      <div className="fixed inset-0 bg-black/70 backdrop-blur-sm flex justify-center items-center z-50">
        <motion.div 
          initial={{ opacity: 0, scale: 0.9 }} 
          animate={{ opacity: 1, scale: 1 }} 
          transition={{ duration: 0.3 }} 
          className={`rounded-2xl p-8 w-full max-w-md ${darkMode ? 'bg-[#1e293b] text-white border border-gray-700' : 'bg-white text-black border border-gray-200'} shadow-2xl relative`}
        >
          <button onClick={() => setShowLoginModal(false)} className="absolute top-4 right-4 text-2xl font-bold hover:text-[#00FFFF] transition">×</button>
          <h2 className={`text-2xl font-bold text-center mb-2 ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'}`}>Login</h2>
          <p className={`text-center text-sm mb-6 ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>
            Enter your credentials and we'll automatically detect your role
          </p>

          <form className="space-y-4" onSubmit={handleLoginSubmit}>
            <input 
              type="email" 
              placeholder="Email" 
              className="w-full border border-gray-300 rounded-xl p-3 text-black focus:border-[#00FFFF] focus:ring-2 focus:ring-[#00FFFF]/20 transition"
              value={loginEmail}
              onChange={(e) => setLoginEmail(e.target.value)}
              required
            />
            <input 
              type="password" 
              placeholder="Password" 
              className="w-full border border-gray-300 rounded-xl p-3 text-black focus:border-[#00FFFF] focus:ring-2 focus:ring-[#00FFFF]/20 transition"
              value={loginPassword}
              onChange={(e) => setLoginPassword(e.target.value)}
              required
            />

            {/* 🆕 Forgot Password Link */}
            <p className="text-center text-sm cursor-pointer text-[#00FFFF] hover:underline" onClick={() => { setShowForgotModal(true); setShowLoginModal(false); }}>
              Forgot Password?
            </p>

            <button 
              type="submit" 
              className={`w-full py-3 rounded-xl font-semibold flex items-center justify-center ${darkMode ? 'bg-[#00FFFF] text-black hover:bg-[#00FFFF]/90' : 'bg-blue-600 text-white hover:bg-blue-700'} transition-all duration-200 shadow-lg hover:shadow-xl`}
              disabled={loading}
            >
              {loading ? (
                <svg className="animate-spin h-5 w-5 mr-3 border-t-2 border-b-2 border-gray-900 rounded-full" viewBox="0 0 24 24"></svg>
              ) : (
                'Login'
              )}
            </button>
          </form>
        </motion.div>
      </div>
    )}

    {/* 🔐 Forgot Password Modal */}
    {showForgotModal && (
      <div className="fixed inset-0 bg-black bg-opacity-70 flex justify-center items-center z-50">
        <motion.div 
          initial={{ opacity: 0, scale: 0.9 }} 
          animate={{ opacity: 1, scale: 1 }} 
          transition={{ duration: 0.3 }} 
          className={`rounded-xl p-8 w-full max-w-md ${darkMode ? 'bg-gray-900 text-white' : 'bg-white text-black'} shadow-2xl relative`}
        >
          <button onClick={() => setShowForgotModal(false)} className="absolute top-2 right-4 text-2xl font-bold">×</button>
          <h2 className="text-2xl font-bold text-center mb-6">Reset Password</h2>

          <form className="space-y-4" onSubmit={handleForgotPasswordSubmit}>
            <input 
              type="email" 
              placeholder="Email" 
              className="w-full border rounded p-2 text-black"
              value={forgotEmail}
              onChange={(e) => setForgotEmail(e.target.value)}
              required
            />
            <input 
              type="password" 
              placeholder="New Password" 
              className="w-full border rounded p-2 text-black"
              value={forgotNewPassword}
              onChange={(e) => setForgotNewPassword(e.target.value)}
              required
            />
            <input 
              type="password" 
              placeholder="Confirm Password" 
              className="w-full border rounded p-2 text-black"
              value={forgotConfirmPassword}
              onChange={(e) => setForgotConfirmPassword(e.target.value)}
              required
            />

            <div className="flex justify-between gap-2">
              {roles.map((role) => (
                <button
                  key={role.value}
                  type="button"
                  onClick={() => setSelectedRole(role.value)}
                  className={`flex-1 py-2 rounded-lg font-semibold border transition ${selectedRole === role.value ? 'bg-yellow-400 text-black' : `${darkMode ? 'bg-gray-700 text-white' : 'bg-gray-100 text-gray-800'}`}`}
                >
                  {role.label}
                </button>
              ))}
            </div>

            <button 
              type="submit" 
              className={`w-full py-2 rounded font-semibold flex items-center justify-center ${darkMode ? 'bg-yellow-400 text-black hover:bg-yellow-500' : 'bg-yellow-600 text-white hover:bg-yellow-700'}`}
              disabled={loading}
            >
              {loading ? (
                <svg className="animate-spin h-5 w-5 mr-3 border-t-2 border-b-2 border-gray-900 rounded-full" viewBox="0 0 24 24"></svg>
              ) : (
                'Reset Password'
              )}
            </button>
          </form>
        </motion.div>
      </div>
    )}



      {/* 🎮 Hero Section */}
      <header className="text-center mt-16 px-6">
        <motion.h1
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.7 }}
          className={`text-5xl font-extrabold ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'} drop-shadow-lg`}
        >
          Unleash Your Skills. Rule the Arena.
        </motion.h1>
        <p className={`mt-4 ${darkMode ? 'text-gray-400' : 'text-gray-600'} text-lg max-w-2xl mx-auto`}>
          Compete in live tournaments, win rewards, and climb the leaderboard with the best.
        </p>
        <motion.button
          whileHover={{ scale: 1.05 }}
          onClick={() => setShowModal(true)}
          className={`mt-8 px-8 py-3 ${darkMode ? 'bg-[#00FFFF] text-black' : 'bg-blue-600 text-white'} font-bold rounded-xl shadow-md hover:shadow-lg transition`}
        >
          Join Now
        </motion.button>
      </header>

      {/* 🎮 Sports Icons Section */}
      <section className={`flex justify-center items-center py-16 ${darkMode ? 'bg-gradient-to-br from-gray-900 via-gray-800 to-gray-950' : 'bg-gradient-to-br from-gray-50 to-white'}`}>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-16 text-center">
          {[{
            icon: <FaFootballBall className={`text-6xl ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'} animate-bounce`} />, label: "Games"
          }, {
            icon: <FaTrophy className={`text-6xl ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'} animate-pulse`} />, label: "Tournaments"
          }, {
            icon: <FaUsers className={`text-6xl ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'} animate-spin-slow`} />, label: "Community"
          }].map((item, i) => (
            <div key={i} className="flex flex-col items-center space-y-4">
              {item.icon}
              <p className={`text-xl font-semibold ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}>{item.label}</p>
            </div>
          ))}
        </div>
      </section>

      {/* 🏆 ONGOING TOURNAMENTS */}
      <section id="ongoing" className={`mt-20 px-10 ${darkMode ? 'bg-gray-800' : 'bg-gray-50'}`}>
        <h2 className={`text-3xl font-semibold ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'} mb-6 flex items-center gap-2`}>
          <FaTrophy /> Ongoing Tournaments
        </h2>
        
        {/* Loading State */}
        {tournamentsLoading && (
          <div className="flex justify-center items-center py-12">
            <div className={`animate-spin rounded-full h-12 w-12 border-b-2 ${darkMode ? 'border-[#00FFFF]' : 'border-blue-600'}`}></div>
            <span className={`ml-4 text-lg ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}>
              Loading tournaments...
            </span>
          </div>
        )}

        {/* Error State */}
        {tournamentsError && (
          <div className="text-center py-12">
            <div className={`text-red-500 text-lg mb-4`}>
              ⚠️ {tournamentsError}
            </div>
            <button 
              onClick={() => window.location.reload()} 
              className={`px-6 py-2 rounded-lg ${darkMode ? 'bg-[#00FFFF] text-black' : 'bg-blue-600 text-white'} hover:opacity-80 transition-opacity`}
            >
              Try Again
            </button>
          </div>
        )}

        {/* Tournament Cards */}
        {!tournamentsLoading && !tournamentsError && (
          <>
            {tournaments.ongoing.length === 0 ? (
              <p className={`${darkMode ? 'text-gray-500' : 'text-gray-600'}`}>No ongoing tournaments right now.</p>
            ) : (
              <div className="grid md:grid-cols-3 gap-8">
                {tournaments.ongoing.map((tournament, i) => (
                  <motion.div
                    key={tournament.id}
                    whileHover={{ scale: 1.05 }}
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, delay: i * 0.1 }}
                    className={`${darkMode ? 'bg-[#1e293b] border-gray-700' : 'bg-white border-gray-200'} p-6 rounded-2xl shadow-lg border hover:border-[#00FFFF]/60 transition`}
                  >
                    <img
                      src={tournament.imageUrl || "/img1.png"}
                      alt={tournament.name}
                      className="rounded-xl mb-4 h-40 w-full object-cover"
                    />
                    <h3 className={`text-xl font-bold ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'}`}>{tournament.name}</h3>
                    <p className={`${darkMode ? 'text-gray-400' : 'text-gray-600'} mt-1 flex items-center gap-2`}>
                      <FaCalendarAlt /> {formatDate(tournament.startDate)} → {formatDate(tournament.endDate)}
                    </p>
                    <p className={`mt-2 ${darkMode ? 'text-gray-300' : 'text-gray-700'} flex items-center gap-2`}>
                      <FaUsers /> {tournament.participants || 0} Players
                    </p>
                    <p className={`mt-2 ${darkMode ? 'text-gray-300' : 'text-gray-700'} flex items-center gap-2`}>
                      <FaDollarSign /> Entry Fee: ${tournament.joiningFee}
                    </p>
                  </motion.div>
                ))}
              </div>
            )}
          </>
        )}
      </section>

      {/* 🏆 UPCOMING TOURNAMENTS */}
      <section id="upcoming" className={`mt-20 px-10 pb-20 ${darkMode ? 'bg-gray-800' : 'bg-gray-50'}`}>
        <h2 className={`text-3xl font-semibold ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'} mb-6 flex items-center gap-2`}>
          <FaCalendarAlt /> Upcoming Battles
        </h2>
        
        {!tournamentsLoading && !tournamentsError && (
          <>
            {tournaments.upcoming.length === 0 ? (
              <p className={`${darkMode ? 'text-gray-500' : 'text-gray-600'}`}>No upcoming tournaments announced.</p>
            ) : (
              <div className="grid md:grid-cols-3 gap-8">
                {tournaments.upcoming.map((tournament, i) => (
                  <motion.div
                    key={tournament.id}
                    whileHover={{ scale: 1.05 }}
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5, delay: i * 0.1 }}
                    className={`${darkMode ? 'bg-[#111827] border-gray-700' : 'bg-white border-gray-200'} p-6 rounded-2xl shadow-lg border hover:border-[#00FFFF]/60 transition`}
                  >
                    <img
                      src={tournament.imageUrl || "/img2.png"}
                      alt={tournament.name}
                      className="rounded-xl mb-4 h-40 w-full object-cover"
                    />
                    <h3 className={`text-xl font-bold ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'}`}>{tournament.name}</h3>
                    <p className={`${darkMode ? 'text-gray-400' : 'text-gray-600'} mt-1 flex items-center gap-2`}>
                      <FaCalendarAlt /> Starts: {formatDate(tournament.startDate)}
                    </p>
                    <p className={`mt-2 ${darkMode ? 'text-gray-300' : 'text-gray-700'} flex items-center gap-2`}>
                      <FaDollarSign /> Entry Fee: ${tournament.joiningFee}
                    </p>
                    <button className={`mt-4 w-full py-2 ${darkMode ? 'bg-[#00FFFF]/20 hover:bg-[#00FFFF]/40' : 'bg-blue-100 hover:bg-blue-200'} rounded-lg transition text-[#00FFFF] font-semibold`}>
                      Pre-Register
                    </button>
                  </motion.div>
                ))}
              </div>
            )}
          </>
        )}
      </section>

      {/* 🧱 Feature Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 px-8 py-12">
  {[{
    title: 'Host Tournaments', desc: 'Manage competitive events with ease.', img: '/img1.png'
  }, {
    title: 'Track Leaderboards', desc: 'View real-time stats and rankings.', img: '/img2.png'
  }, {
    title: 'Join Competitions', desc: 'Compete globally with fellow players.', img: '/img3.png'
  }].map((card, i) => (
    <div key={i} className={`p-5 rounded-lg shadow-lg transition ${darkMode ? 'bg-gray-800 hover:shadow-yellow-500/30' : 'bg-white hover:shadow-lg'}`}>
      <img src={card.img} alt={card.title} className="rounded-md mb-4 w-full h-auto object-cover" />
      <h3 className={`text-xl font-semibold mb-2 ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`}>{card.title}</h3>
      <p className={darkMode ? 'text-gray-300' : 'text-gray-600'}>{card.desc}</p>
    </div>
  ))}
</div>

      {/* 🧠 About Section */}
      <div ref={aboutRef} id="about" className={`${darkMode ? 'bg-gray-900 text-white' : 'bg-gray-100 text-black'} px-8 py-16`}>
        <div className={`relative ${darkMode ? 'bg-gray-800' : 'bg-white'} bg-opacity-40 backdrop-blur-lg rounded-xl p-10 shadow-2xl border border-gray-700 mx-auto max-w-4xl`}>
          <h2 className={`text-4xl font-bold mb-6 text-center ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`}>About Titan E-sports</h2>
          <div className={`space-y-5 text-lg leading-relaxed ${darkMode ? 'text-gray-300' : 'text-gray-700'}`}>
            <p><strong className={darkMode ? 'text-yellow-400' : 'text-yellow-600'}>Titan E-sports</strong> is your digital battleground, bringing players, hosts, and fans into a unified, electrifying sports experience.</p>
            <p>⚽ <strong>Tournaments</strong> bring the best together. We help organize and manage every phase with ease.</p>
            <p>🏆 <strong>Leaderboard</strong> keeps the fire alive, tracking every move, win, and stat in real-time.</p>
            <p>🌍 <strong>Global Platform</strong> means competition without borders. Join players from all around the world.</p>
          </div>
        </div>
      </div>

      {/* 📎 Enhanced Footer */}
      <footer className={`text-center py-6 border-t ${darkMode ? 'border-gray-700 bg-black/30' : 'border-gray-200 bg-gray-50'} ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>
        <div className="container mx-auto px-6 flex flex-col md:flex-row justify-between items-center">
          <div className="text-center md:text-left mb-4 md:mb-0">
            <h3 className={`text-2xl font-bold ${darkMode ? 'text-[#00FFFF]' : 'text-blue-600'}`}>TITAN ESPORTS ⚡</h3>
            <p className={`text-sm ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>© 2025 Titan Esports. All Rights Reserved.</p>
          </div>
          <div className="flex space-x-6">
            <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-[#00FFFF]' : 'text-gray-600 hover:text-blue-600'}`}>
              <FaTwitter />
            </a>
            <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-[#00FFFF]' : 'text-gray-600 hover:text-blue-600'}`}>
              <FaInstagram />
            </a>
            <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-[#00FFFF]' : 'text-gray-600 hover:text-blue-600'}`}>
              <FaFacebookF />
            </a>
            <a href="https://youtube.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-[#00FFFF]' : 'text-gray-600 hover:text-blue-600'}`}>
              <FaYoutube />
            </a>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default HomePage;
