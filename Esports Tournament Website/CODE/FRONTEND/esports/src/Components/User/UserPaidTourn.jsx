import React, { useState, useEffect } from 'react';
import '@fontsource/poppins';
import { FaTrophy, FaSignOutAlt, FaSun, FaMoon, FaTwitter, FaInstagram, FaFacebookF, FaYoutube } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import jsPDF from 'jspdf'; // 📄 For PDF
import QRCode from 'qrcode'; // 📲 For QR code
import axios from 'axios'; // For making API requests
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const PaidTournamentsPage = () => {
  const [darkMode, setDarkMode] = useState(true);
  const [paidTournaments, setPaidTournaments] = useState([]); // Empty array to hold fetched tournaments
  const [countdowns, setCountdowns] = useState({});
  const [loading, setLoading] = useState(true); // To handle loading state
  const [error, setError] = useState(null); // For handling errors

  const playerId = sessionStorage.getItem('playerId'); // Assuming playerId is stored in sessionStorage

  // Fetch paid tournaments from the API
  useEffect(() => {
    const fetchPaidTournaments = async () => {
      try {
        setLoading(true);
  const response = await axios.get(`/api/player/paid-tournaments/${playerId}`);
        if (response.data.status === 'success') {
          setPaidTournaments(response.data.data); // Assuming the data is under 'data' key
        } else {
          toast.error('Failed to fetch paid tournaments.');
        }
      } catch (error) {
        setError('Error fetching paid tournaments.');
        toast.error('Error fetching paid tournaments.');
      } finally {
        setLoading(false);
      }
    };

    fetchPaidTournaments();
  }, [playerId]);

  // Function to calculate countdown to the tournament start date
  const calculateCountdown = (startDate) => {
    const now = new Date();
    const start = new Date(startDate);
    const timeDifference = start - now;

    if (timeDifference <= 0) {
      return 'Tournament has already started!';
    }

    const days = Math.floor(timeDifference / (1000 * 60 * 60 * 24));
    const hours = Math.floor((timeDifference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((timeDifference % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((timeDifference % (1000 * 60)) / 1000);

    return `${days}d ${hours}h ${minutes}m ${seconds}s`;
  };

  useEffect(() => {
    const interval = setInterval(() => {
      const updatedCountdowns = {};
      paidTournaments.forEach((tournament) => {
        updatedCountdowns[tournament.id] = calculateCountdown(tournament.startDate);
      });
      setCountdowns(updatedCountdowns);
    }, 1000);

    return () => clearInterval(interval);
  }, [paidTournaments]);

  // 🎟️ Function to download ticket with QR code
  const downloadTicket = async (tournament) => {
    const doc = new jsPDF();
  
    // Header background
    doc.setFillColor(40, 116, 166);
    doc.rect(0, 0, 210, 30, 'F');
    doc.setTextColor(255, 255, 255);
    doc.setFontSize(18);
    doc.text('🏆 Titan E-sports Tournament Ticket', 105, 20, { align: 'center' });
  
    // Tournament info
    const infoY = 50;
    const gap = 10;
  
    doc.setFontSize(14);
    doc.setTextColor(0, 0, 0);
    doc.text('🎮 Tournament Name:', 20, infoY);
    doc.setFontSize(12);
    doc.text(tournament.name, 80, infoY);
  
    doc.setFontSize(14);
    doc.text('🧑 Host ID:', 20, infoY + gap);
    doc.setFontSize(12);
    doc.text(tournament.hostId, 80, infoY + gap);
  
    doc.setFontSize(14);
    doc.text('📅 Start Date:', 20, infoY + 2 * gap);
    doc.setFontSize(12);
    doc.text(tournament.startDate.substring(0, 10), 80, infoY + 2 * gap);
  
    doc.setFontSize(14);
    doc.text('📅 End Date:', 20, infoY + 3 * gap);
    doc.setFontSize(12);
    doc.text(tournament.endDate.substring(0, 10), 80, infoY + 3 * gap);
  
    doc.setFontSize(14);
    doc.setTextColor(34, 139, 34);
    doc.text('✔ Payment Status:', 20, infoY + 4 * gap);
    doc.setFontSize(12);
    doc.text('Paid', 80, infoY + 4 * gap);
  
    // QR Code
    const qrText = `Tournament: ${tournament.name}\nHost ID: ${tournament.hostId}\nPayment: Paid`;
    const qrDataURL = await QRCode.toDataURL(qrText);
    doc.addImage(qrDataURL, 'PNG', 140, infoY - 10, 50, 50);
  
    // Border
    doc.setDrawColor(40, 116, 166);
    doc.rect(10, 35, 190, 130);
  
    // Footer
    doc.setFontSize(10);
    doc.setTextColor(150);
    doc.text('Thank you for registering. Visit titanesports.com for more info.', 105, 170, { align: 'center' });
  
    // Save
    doc.save(`${tournament.name}_Ticket.pdf`);
  };
  

  return (
    <div className={`${darkMode ? 'bg-gray-950 text-white' : 'bg-white text-black'} font-poppins min-h-screen flex flex-col transition-colors duration-300`}>
      <ToastContainer />

      {/* 🌐 Navbar */}
      <nav className={`${darkMode ? 'bg-gray-900' : 'bg-gray-100'} shadow-md py-4 px-8 flex justify-between items-center`}>
        <div className="flex items-center space-x-3">
          <FaTrophy className={`text-3xl ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`} />
          <h1 className={`text-2xl font-bold ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`}>Paid Tournaments</h1>
        </div>
        <div className="space-x-6 flex items-center">
          <Link to="/viewTourn" className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>View Tournaments</Link>
          <Link to="/userPaidTourn" className={`transition ${darkMode ? 'text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Paid Tournaments</Link>
          <Link to="/leaderboard" className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Leaderboard</Link>
          <Link to="/profile" className={`transition ${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Profile</Link>
          <button onClick={() => setDarkMode(!darkMode)} className="ml-4 p-2 rounded-full border border-gray-400">
            {darkMode ? <FaSun className="text-yellow-300" /> : <FaMoon className="text-gray-800" />}
          </button>
        </div>
      </nav>

      {/* 🏆 Paid Tournaments Section */}
      <section className={`py-16 px-8 ${darkMode ? 'bg-gray-900 text-white' : 'bg-gray-100 text-black'}`}>
        <div className="max-w-6xl mx-auto">
          <h2 className="text-3xl font-bold mb-8 text-center">Your Paid Tournaments</h2>

          {loading && <p className="text-center text-yellow-400">Loading paid tournaments...</p>}
          {error && <p className="text-center text-red-500">{error}</p>}

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
            {paidTournaments.length > 0 ? paidTournaments.map((tournament) => (
              <div key={tournament.id} className="bg-gray-700 p-6 rounded-lg shadow-lg">
                <img src={tournament.imageUrl || '/tourn1.avif'} alt={tournament.name} className="w-full h-48 object-cover rounded-lg mb-4" />
                <h4 className="text-xl font-semibold mb-2">{tournament.name}</h4>
                <p className="mb-2">{tournament.description}</p>
                <p><strong>Host ID:</strong> {tournament.hostId}</p>
                <p><strong>Start:</strong> {tournament.startDate.substring(0, 10)}</p>
                <p><strong>End:</strong> {tournament.endDate.substring(0, 10)}</p>
                <p className="text-lime-400 mt-2"><strong>Countdown:</strong> {countdowns[tournament.id]}</p>
                <p className="text-green-400 font-semibold mt-2">✔️ Payment Completed</p>

                {/* 🎟️ Ticket and WhatsApp Join */}
                <div className="flex justify-center gap-4 mt-4">
                  <button
                    onClick={() => downloadTicket(tournament)}
                    className="bg-green-500 hover:bg-green-600 text-white font-semibold px-4 py-2 rounded-full transition"
                  >
                    Download Ticket
                  </button>

                  <a
                    href={'https://chat.whatsapp.com/LDTh8hMr7ik9zxjG3WfOm2'}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="bg-blue-500 hover:bg-blue-600 text-white font-semibold px-4 py-2 rounded-full transition"
                  >
                    Join WhatsApp
                  </a>
                </div>
              </div>
            )) : (
              <p>No paid tournaments available.</p>
            )}
          </div>
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

export default PaidTournamentsPage;
