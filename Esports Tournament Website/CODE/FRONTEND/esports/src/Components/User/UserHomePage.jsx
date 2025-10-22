import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import '@fontsource/poppins';
import {
  FaTrophy, FaUsers, FaMoneyBillAlt, FaMedal,
  FaSignOutAlt, FaUser, FaSun, FaMoon,
  FaInstagram, FaTwitter, FaFacebookF, FaYoutube
} from 'react-icons/fa';

const UserHomePage = () => {
  const [darkMode, setDarkMode] = useState(true);
  const navigate = useNavigate();

  const handleLogout = () => {
    sessionStorage.clear();
    toast.success('User logged out successfully!');
    navigate('/');
  };

  return (
    <div className={`${darkMode ? 'bg-gray-950 text-white' : 'bg-white text-black'} font-poppins min-h-screen flex flex-col transition-colors duration-300`}>
      <ToastContainer />

      {/* Navbar */}
      <nav className={`${darkMode ? 'bg-gray-900' : 'bg-gray-100'} shadow-md py-4 px-8 flex justify-between items-center`}>
        <div className="flex items-center space-x-3">
          <FaTrophy className={`text-3xl ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`} />
          <h1 className={`text-2xl font-bold ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`}>User Dashboard</h1>
        </div>
        <div className="space-x-6 flex items-center">
          <Link to="/viewTourn" className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>View Tournaments</Link>
          <Link to="/userPaidTourn" className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Paid Tournaments</Link>
          <Link to="/leaderboard" className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Leaderboard</Link>
          <Link to="/profile" className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Profile</Link>
          <button onClick={handleLogout} className="transition text-red-500 hover:text-red-700 flex items-center">
            <FaSignOutAlt className="inline mr-2" /> Logout
          </button>
          <button onClick={() => setDarkMode(!darkMode)} className="ml-4 p-2 rounded-full border border-gray-400">
            {darkMode ? <FaSun className="text-yellow-300" /> : <FaMoon className="text-gray-800" />}
          </button>
        </div>
      </nav>

      {/* Welcome Section */}
      <section className={`py-16 px-8 ${darkMode ? 'bg-gray-900 text-white' : 'bg-gray-100 text-black'}`}>
        <div className="max-w-4xl mx-auto text-center">
          <h2 className={`text-4xl font-bold mb-6 ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`}>Welcome to Your Dashboard</h2>
          <p className="text-lg leading-relaxed">
            Manage your tournaments, join events, view your payments, and check the leaderboard here. Keep your profile updated!
          </p>
        </div>
      </section>

      {/* Cards Section */}
      <section className={`py-16 px-8 ${darkMode ? 'bg-gray-900 text-white' : 'bg-gray-100 text-black'}`}>
        <div className="max-w-6xl mx-auto grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8 text-center">
          <Link to="/viewTourn" className={`block p-5 rounded-lg shadow-lg transition transform hover:-translate-y-1 ${darkMode ? 'bg-gray-800 hover:shadow-yellow-500/30' : 'bg-white hover:shadow-lg'}`}>
            <FaTrophy className="text-5xl mb-4 text-yellow-400" />
            <h3 className="text-xl font-semibold mb-2">View Tournaments</h3>
            <p>Browse available tournaments and explore details.</p>
          </Link>
          <Link to="/joinTourn" className={`block p-5 rounded-lg shadow-lg transition transform hover:-translate-y-1 ${darkMode ? 'bg-gray-800 hover:shadow-yellow-500/30' : 'bg-white hover:shadow-lg'}`}>
            <FaUsers className="text-5xl mb-4 text-yellow-400" />
            <h3 className="text-xl font-semibold mb-2">Join Tournament</h3>
            <p>Register and participate in tournaments of your choice.</p>
          </Link>
          <Link to="/userPaidTourn" className={`block p-5 rounded-lg shadow-lg transition transform hover:-translate-y-1 ${darkMode ? 'bg-gray-800 hover:shadow-yellow-500/30' : 'bg-white hover:shadow-lg'}`}>
            <FaMoneyBillAlt className="text-5xl mb-4 text-yellow-400" />
            <h3 className="text-xl font-semibold mb-2">Paid Tournaments</h3>
            <p>Track tournaments you have registered with payments.</p>
          </Link>
          <Link to="/leaderboard" className={`block p-5 rounded-lg shadow-lg transition transform hover:-translate-y-1 ${darkMode ? 'bg-gray-800 hover:shadow-yellow-500/30' : 'bg-white hover:shadow-lg'}`}>
            <FaMedal className="text-5xl mb-4 text-yellow-400" />
            <h3 className="text-xl font-semibold mb-2">Leaderboard</h3>
            <p>Check your ranking and compare with others.</p>
          </Link>
          <Link to="/profile" className={`block p-5 rounded-lg shadow-lg transition transform hover:-translate-y-1 ${darkMode ? 'bg-gray-800 hover:shadow-yellow-500/30' : 'bg-white hover:shadow-lg'}`}>
            <FaUser className="text-5xl mb-4 text-yellow-400" />
            <h3 className="text-xl font-semibold mb-2">Profile</h3>
            <p>Update your profile details and manage your account.</p>
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer className={`${darkMode ? 'bg-gray-900' : 'bg-gray-100'} py-8 mt-12 border-t border-gray-800`}>
        <div className="container mx-auto px-6 flex flex-col md:flex-row justify-between items-center">
          <div className="text-center md:text-left mb-4 md:mb-0">
            <h3 className={`text-2xl font-bold ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`}>Titan E-sports</h3>
            <p className={`text-sm ${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>© 2025 Titan E-sports. All rights reserved.</p>
          </div>
          <div className="flex space-x-6">
            <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-yellow-400' : 'text-gray-600 hover:text-yellow-600'}`}>
              <FaTwitter />
            </a>
            <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-yellow-400' : 'text-gray-600 hover:text-yellow-600'}`}>
              <FaInstagram />
            </a>
            <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-yellow-400' : 'text-gray-600 hover:text-yellow-600'}`}>
              <FaFacebookF />
            </a>
            <a href="https://youtube.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-yellow-400' : 'text-gray-600 hover:text-yellow-600'}`}>
              <FaYoutube />
            </a>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default UserHomePage;
