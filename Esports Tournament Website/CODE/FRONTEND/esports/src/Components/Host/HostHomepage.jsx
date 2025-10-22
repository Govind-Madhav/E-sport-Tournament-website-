import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { toast, ToastContainer } from 'react-toastify';
import '@fontsource/poppins';
import 'react-toastify/dist/ReactToastify.css';
import { motion } from 'framer-motion';
import {
  FaTrophy, FaUsers, FaMoneyBillAlt, FaMedal, FaSignOutAlt,
  FaInstagram, FaTwitter, FaFacebookF, FaYoutube, FaSun, FaMoon
} from 'react-icons/fa';

const HostPage = () => {
  const [darkMode, setDarkMode] = useState(true);
  const navigate = useNavigate();

  const handleLogout = () => {
    sessionStorage.clear();
    toast.success('✅ Host Logged out successfully');
    navigate('/');
  };

  const cardVariants = {
    hover: {
      scale: 1.05,
      boxShadow: "0 0 20px rgba(255, 215, 0, 0.5)",
    },
  };

  return (
    <div className={`${darkMode ? 'bg-gray-950 text-white' : 'bg-white text-black'} font-poppins min-h-screen flex flex-col transition-colors duration-300`}>
      <ToastContainer />

      {/* 🌐 Navbar */}
      <nav className={`${darkMode ? 'bg-gray-900' : 'bg-gray-100'} shadow-md py-4 px-8 flex justify-between items-center`}>
        <div className="flex items-center space-x-3">
          <FaTrophy className="text-3xl text-yellow-400" />
          <h1 className="text-2xl font-bold text-yellow-400">Host Dashboard</h1>
        </div>
        <div className="space-x-6 flex items-center">
          <Link to="/hostTourn" className="hover:text-yellow-400 transition">Manage Tournaments</Link>
          <Link to="/hostUsers" className="hover:text-yellow-400 transition">View Users</Link>
          <Link to="/hostUsersPayment" className="hover:text-yellow-400 transition">View Payments</Link>
          <Link to="/declare-winners" className="hover:text-yellow-400 transition">Declare Winners</Link>
          <button className="text-red-500 hover:text-red-700 transition" onClick={handleLogout}>
            <FaSignOutAlt className="inline mr-2" />
            Logout
          </button>
          <button onClick={() => setDarkMode(!darkMode)} className="ml-4 p-2 rounded-full border border-gray-500 hover:scale-105 transition">
            {darkMode ? <FaSun className="text-yellow-300" /> : <FaMoon className="text-gray-800" />}
          </button>
        </div>
      </nav>

      {/* 🧑‍💻 Welcome Section */}
      <section className="py-16 px-6 text-center">
        <h2 className="text-4xl font-bold mb-4 text-yellow-400">Welcome to Host Dashboard</h2>
        <p className="text-lg max-w-2xl mx-auto">As a host on <span className="font-semibold text-yellow-300">Titan Esports</span>, you can manage tournaments, view users, track payments, and declare winners.</p>
      </section>

      {/* 🎮 Feature Cards */}
      <section className={`py-16 px-8 ${darkMode ? 'bg-gray-900 text-white' : 'bg-gray-100 text-black'}`}>
  <div className="max-w-6xl mx-auto grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8 text-center">

    <Link
      to="/hostTourn"
      className={`p-5 rounded-lg shadow-lg transition duration-300 transform hover:scale-105 cursor-pointer 
        ${darkMode ? 'bg-gray-800 hover:shadow-yellow-400/50' : 'bg-white hover:shadow-yellow-300/50'}`}
    >
      <FaTrophy className="text-5xl mb-4 text-yellow-400" />
      <h3 className="text-xl font-semibold mb-2">Manage Tournaments</h3>
      <p>Create, update, and organize your tournaments with ease. Ensure everything runs smoothly from start to finish.</p>
    </Link>

    <Link
      to="/hostUsers"
      className={`p-5 rounded-lg shadow-lg transition duration-300 transform hover:scale-105 cursor-pointer 
        ${darkMode ? 'bg-gray-800 hover:shadow-yellow-400/50' : 'bg-white hover:shadow-yellow-300/50'}`}
    >
      <FaUsers className="text-5xl mb-4 text-yellow-400" />
      <h3 className="text-xl font-semibold mb-2">View Users</h3>
      <p>Monitor users participating in your tournaments and manage their registrations effectively.</p>
    </Link>

    <Link
      to="/hostUsersPayment"
      className={`p-5 rounded-lg shadow-lg transition duration-300 transform hover:scale-105 cursor-pointer 
        ${darkMode ? 'bg-gray-800 hover:shadow-yellow-400/50' : 'bg-white hover:shadow-yellow-300/50'}`}
    >
      <FaMoneyBillAlt className="text-5xl mb-4 text-yellow-400" />
      <h3 className="text-xl font-semibold mb-2">View Payments</h3>
      <p>Track payment status for registrations and ensure smooth financial transactions for your events.</p>
    </Link>

    <Link
      to="/declare-winners"
      className={`p-5 rounded-lg shadow-lg transition duration-300 transform hover:scale-105 cursor-pointer 
        ${darkMode ? 'bg-gray-800 hover:shadow-yellow-400/50' : 'bg-white hover:shadow-yellow-300/50'}`}
    >
      <FaMedal className="text-5xl mb-4 text-yellow-400" />
      <h3 className="text-xl font-semibold mb-2">Declare Winners</h3>
      <p>After the tournament, declare winners and keep participants updated with their achievements.</p>
    </Link>

  </div>
</section>


      {/* 📎 Footer */}
      <footer className={`${darkMode ? 'bg-gray-900' : 'bg-gray-100'} py-8 mt-auto border-t border-gray-800`}>
        <div className="container mx-auto px-6 flex flex-col md:flex-row justify-between items-center">
          <div className="text-center md:text-left mb-4 md:mb-0">
            <h3 className="text-2xl font-bold text-yellow-400">Titan E-sports</h3>
            <p className="text-sm text-gray-400">© 2025 Titan E-sports. All rights reserved.</p>
          </div>
          <div className="flex space-x-6">
            {[FaTwitter, FaInstagram, FaFacebookF, FaYoutube].map((Icon, i) => (
              <a
                key={i}
                href="#"
                target="_blank"
                rel="noopener noreferrer"
                className="transition text-xl text-gray-400 hover:text-yellow-400"
              >
                <Icon />
              </a>
            ))}
          </div>
        </div>
      </footer>
    </div>
  );
};

export default HostPage;
