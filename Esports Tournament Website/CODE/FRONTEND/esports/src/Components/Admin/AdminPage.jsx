import React, { useState } from 'react';
import '@fontsource/poppins';
import { FaTrophy, FaUsers, FaUsersCog, FaSignOutAlt, FaInstagram, FaTwitter, FaFacebookF, FaYoutube, FaSun, FaMoon } from 'react-icons/fa';
import { Link, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';

const AdminPage = () => {
  const [darkMode, setDarkMode] = useState(true);

  const navigate = useNavigate();

  
  const handleLogout = () => {
    sessionStorage.clear();
    toast.success("Admin logged out successfully");
    navigate('/');
  }


  return (
    <div className={`${darkMode ? 'bg-gray-950 text-white' : 'bg-white text-black'} font-poppins min-h-screen flex flex-col transition-colors duration-300`}>
      
      {/* 🌐 Navbar */}
      <nav className={`${darkMode ? 'bg-gray-900' : 'bg-gray-100'} shadow-md py-4 px-8 flex justify-between items-center`}>
        <div className="flex items-center space-x-3">
          <FaTrophy className={`text-3xl ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`} />
          <Link to={"/adminPage"}><h1 className={`text-2xl font-bold ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`}>Admin Dashboard</h1></Link>
        </div>
        <div className="space-x-6 flex items-center">
          <Link to={"/manageUsers"}><button className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Players</button></Link>
          <Link to={"/manageHosts"}><button className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Hosts</button></Link>
          <Link to={"/manageTourn"}><button className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Tournaments</button></Link>
          <Link to={"/manageTrans"}><button className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Transactions</button></Link>
          <button className="transition text-red-500 hover:text-red-700" onClick={handleLogout}>
            <FaSignOutAlt className="inline mr-2" />
            Logout
          </button>
          <button onClick={() => setDarkMode(!darkMode)} className="ml-4 p-2 rounded-full border border-gray-400">
            {darkMode ? <FaSun className="text-yellow-300" /> : <FaMoon className="text-gray-800" />}
          </button>
        </div>
      </nav>

        {/* 🎮 Background Sports Animation Section with Moving Circles */}
        <section className={`relative py-24 bg-gradient-to-br from-gray-900 via-gray-800 to-gray-950`}>
        {/* Animated Circles */}
            <div className="absolute inset-0 z-10 opacity-30">
            <div className="animate-bounce absolute top-1/4 left-1/4 transform -translate-x-1/2 translate-y-1/2 w-32 h-32 bg-yellow-400 rounded-full opacity-60"></div>
            <div className="animate-bounce absolute top-1/3 left-3/4 transform -translate-x-1/2 translate-y-1/2 w-24 h-24 bg-yellow-400 rounded-full opacity-60"></div>
            <div className="animate-bounce absolute top-1/2 left-1/4 transform -translate-x-1/2 translate-y-1/2 w-36 h-36 bg-yellow-400 rounded-full opacity-60"></div>
            </div>
            <div className="relative z-20 text-center text-white">
            <h3 className="text-3xl font-semibold">Welcome to the Admin Dashboard</h3>
            <p className="mt-4 text-lg">Manage your platform’s content efficiently.</p>
            </div>
        </section>

        {/* 🧑‍💼 Features and Management Section */}
      <section className="py-16 px-8">
        <div className="max-w-6xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
          {/* Manage Users */}
          <div className="p-5 rounded-lg shadow-lg transition ${darkMode ? 'bg-gray-800 hover:shadow-yellow-500/30' : 'bg-white hover:shadow-lg'}">
            <FaUsersCog className="text-5xl mb-4 text-yellow-400" />
            <h3 className="text-xl font-semibold mb-2">Manage Users</h3>
            <p>Review, approve, or reject users. Assign appropriate roles and manage accounts efficiently.</p>
          </div>
          {/* Manage Hosts */}
          <div className="p-5 rounded-lg shadow-lg transition ${darkMode ? 'bg-gray-800 hover:shadow-yellow-500/30' : 'bg-white hover:shadow-lg'}">
            <FaUsers className="text-5xl mb-4 text-yellow-400" />
            <h3 className="text-xl font-semibold mb-2">Manage Hosts</h3>
            <p>Oversee the hosts who are organizing tournaments and ensure everything runs smoothly.</p>
          </div>
          {/* Manage Tournaments */}
          <div className="p-5 rounded-lg shadow-lg transition ${darkMode ? 'bg-gray-800 hover:shadow-yellow-500/30' : 'bg-white hover:shadow-lg'}">
            <FaTrophy className="text-5xl mb-4 text-yellow-400" />
            <h3 className="text-xl font-semibold mb-2">Manage Tournaments</h3>
            <p>Schedule, update, or cancel tournaments. Track rankings and player progress.</p>
          </div>
        </div>
      </section>


      {/* 🧑‍💻 Admin About Section */}
      <section className={`py-16 px-8 ${darkMode ? 'bg-gray-900 text-white' : 'bg-gray-100 text-black'}`}>
        <div className="max-w-4xl mx-auto text-center">
          <h2 className={`text-4xl font-bold mb-6 ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`}>About Admin Dashboard</h2>
          <p className="text-lg leading-relaxed">
            As the admin of Titan E-sports, you are responsible for overseeing and managing all aspects of the platform. You can manage users, hosts, tournaments, and transactions in this dashboard.
            From approving or rejecting user registrations, managing tournament schedules, to monitoring transactions, the admin role is essential for maintaining the platform's integrity.
          </p>
        </div>
      </section>

      

      

      {/* 📎 Footer */}
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

export default AdminPage;
