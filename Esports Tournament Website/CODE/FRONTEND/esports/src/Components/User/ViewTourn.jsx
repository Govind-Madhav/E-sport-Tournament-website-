import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '@fontsource/poppins';
import { FaTrophy, FaSignOutAlt, FaSun, FaMoon, FaCheckCircle, FaTwitter, FaInstagram, FaFacebookF, FaYoutube } from 'react-icons/fa';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const PaymentPage = () => {
  const [darkMode, setDarkMode] = useState(true);
  const [tournaments, setTournaments] = useState([]);
  const [selectedTournament, setSelectedTournament] = useState(null);
  const [paymentMethod, setPaymentMethod] = useState('Card');
  const [cardNumber, setCardNumber] = useState('');
  const [cvv, setCvv] = useState('');
  const [validThru, setValidThru] = useState('');
  const [upiId, setUpiId] = useState('');
  const [isValidUpi, setIsValidUpi] = useState(false);
  const [paymentSuccess, setPaymentSuccess] = useState(false);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const navigate = useNavigate();
  const playerId = sessionStorage.getItem('playerId');

  useEffect(() => {
    const fetchTournaments = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/player/active-tournaments');
        if (response.data.status === 'success') {
          setTournaments(response.data.data);
        } else {
          toast.error('Failed to fetch tournaments.');
        }
      } catch (error) {
        toast.error('Error fetching tournaments.');
      }
    };
    fetchTournaments();
  }, []);

  // Step 1: First Join the tournament
  const handleJoinTournament = async (tournament) => {
    try {
      const response = await axios.post(`http://localhost:8080/api/player/join/${playerId}/${tournament.id}`);
      if (response.data.status === 'success') {
        toast.success('Successfully requested to join tournament!');
        setSelectedTournament(tournament);
        setShowPaymentModal(true); // Only open payment after join successful
      } else {
        toast.error(response.data.message || 'Failed to join tournament.');
      }
    } catch (error) {
      toast.error(error.response?.data?.message || 'Error joining tournament.');
    }
  };

  // Step 2: After join is successful, Payment
  const handlePayment = async () => {
    if (!selectedTournament) {
      toast.error('No tournament selected!');
      return;
    }

    const paymentData = {
      playerId,
      tournamentId: selectedTournament.id,
      amount: selectedTournament.joiningFee,
      paymentMethod,
      status: true,
      cardNumber,
      cardExpiryDate: validThru,
      cardCVC: cvv,
      upiId
    };

    try {
      const response = await axios.post(`http://localhost:8080/api/player/make-payment/${playerId}/${selectedTournament.id}`, paymentData);
      if (response.data.status === 'success') {
        setPaymentSuccess(true);
        setTimeout(() => {
          setPaymentSuccess(false);
          navigate('/userHomePage');
        }, 2000);
      } else {
        toast.error('Payment failed!');
      }
    } catch (error) {
      toast.error('Payment failed!');
    }
  };

  const handleCardNumberChange = (e) => {
    const value = e.target.value.replace(/[^0-9]/g, '');
    if (value.length <= 16) {
      setCardNumber(value);
    }
  };

  const handleCvvChange = (e) => {
    const value = e.target.value.replace(/[^0-9]/g, '');
    if (value.length <= 3) {
      setCvv(value);
    }
  };

  const handleUpiChange = (e) => {
    const value = e.target.value;
    setUpiId(value);
    setIsValidUpi(/^[a-zA-Z0-9]+@[a-zA-Z0-9.-]+$/.test(value));
  };

  return (
    <div className={`${darkMode ? 'bg-gray-950 text-white' : 'bg-white text-black'} font-poppins min-h-screen flex flex-col transition-colors duration-300`}>
      <ToastContainer />

      {/* Navbar */}
      <nav className={`${darkMode ? 'bg-gray-900' : 'bg-gray-100'} shadow-md py-4 px-8 flex justify-between items-center`}>
        <div className="flex items-center space-x-3">
          <FaTrophy className={`text-3xl ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`} />
          <Link to={'/userHomePage'}><h1 className={`text-2xl font-bold ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`}>View Tournaments</h1></Link>
        </div>
        <div className="space-x-6 flex items-center">
          <Link to="/userHomePage" className={`${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Home</Link>
          <Link to="/viewTourn" className={`${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>View Tournaments</Link>
          <Link to="/userPaidTourn" className={`${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Paid Tournaments</Link>
          <Link to="/leaderboard" className={`${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Leaderboard</Link>
          <Link to="/profile" className={`${darkMode ? 'text-white hover:text-yellow-400' : 'text-gray-800 hover:text-yellow-600'}`}>Profile</Link>
          <button onClick={() => setDarkMode(!darkMode)} className="ml-4 p-2 rounded-full border border-gray-400">
            {darkMode ? <FaSun className="text-yellow-300" /> : <FaMoon className="text-gray-800" />}
          </button>
        </div>
      </nav>

      {/* Active Tournaments Section */}
      <section className={`py-16 px-8 ${darkMode ? 'bg-gray-900 text-white' : 'bg-gray-100 text-black'}`}>
        <div className="max-w-6xl mx-auto">
          <h2 className="text-3xl font-bold mb-8 text-center">Active Tournaments</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
            {tournaments.length > 0 ? tournaments.map(tournament => (
              <div key={tournament.id} className="bg-gray-700 p-6 rounded-lg shadow-lg">
                <img src={tournament.imageUrl || '/tourn1.avif'} alt={tournament.name} className="w-full h-48 object-cover rounded-lg mb-4" />
                <h4 className="text-xl font-semibold mb-2">{tournament.name}</h4>
                <p className="mb-2">{tournament.description}</p>
                <p><strong>Start:</strong> {tournament.startDate}</p>
                <p><strong>End:</strong> {tournament.endDate}</p>
                <p className="text-lime-400 mt-2"><strong>Joining Fee:</strong> ₹{tournament.joiningFee}</p>
                <button
                  onClick={() => handleJoinTournament(tournament)}
                  className="bg-yellow-400 hover:bg-yellow-500 text-black font-semibold px-6 py-2 rounded-full mt-4"
                >
                  Join Tournament
                </button>
              </div>
            )) : (
              <p>No active tournaments available.</p>
            )}
          </div>
        </div>
      </section>

      {/* Payment Modal */}
      {showPaymentModal && selectedTournament && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
          <div className={`p-8 rounded-lg ${darkMode ? 'bg-gray-800 text-white' : 'bg-white text-black'} w-96`}>
            <h3 className="text-2xl font-bold mb-4 text-center">Payment for {selectedTournament.name}</h3>

            <div>
              <label className="block text-sm font-semibold mb-2">Payment Method</label>
              <select
                value={paymentMethod}
                onChange={(e) => setPaymentMethod(e.target.value)}
                className="border p-2 rounded w-full mb-4 text-black"
              >
                <option value="Card">Card</option>
                <option value="UPI">UPI</option>
              </select>

              {paymentMethod === 'Card' && (
                <>
                  <input
                    type="text"
                    placeholder="Card Number"
                    value={cardNumber}
                    onChange={handleCardNumberChange}
                    className="border p-2 rounded w-full mb-4 text-black"
                    maxLength={16}
                  />
                  <input
                    type="text"
                    placeholder="CVV"
                    value={cvv}
                    onChange={handleCvvChange}
                    className="border p-2 rounded w-full mb-4 text-black"
                    maxLength={3}
                  />
                  <input
                    type="text"
                    placeholder="Valid Thru (MM/YY)"
                    value={validThru}
                    onChange={(e) => setValidThru(e.target.value)}
                    className="border p-2 rounded w-full mb-4 text-black"
                  />
                </>
              )}

              {paymentMethod === 'UPI' && (
                <input
                  type="text"
                  placeholder="123456789@ybl"
                  value={upiId}
                  onChange={handleUpiChange}
                  className={`border p-2 rounded w-full mb-4 text-black ${isValidUpi ? 'border-green-300' : 'border-red-300'}`}
                />
              )}

              <button
                onClick={handlePayment}
                className="bg-green-500 hover:bg-green-600 text-white w-full py-3 rounded-lg font-semibold mt-4"
              >
                Make Payment
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Payment Success */}
      {paymentSuccess && (
        <div className="fixed inset-0 bg-black bg-opacity-60 flex justify-center items-center z-50">
          <div className="bg-green-500 p-8 rounded-lg flex flex-col items-center">
            <FaCheckCircle className="text-5xl mb-4 text-white animate-bounce" />
            <h2 className="text-3xl font-bold text-white">Payment Successful!</h2>
          </div>
        </div>
      )}

      {/* Footer */}
      <footer className={`${darkMode ? 'bg-gray-900' : 'bg-gray-100'} py-8 mt-12 border-t border-gray-800`}>
        <div className="container mx-auto px-6 flex flex-col md:flex-row justify-between items-center">
          <div className="text-center md:text-left mb-4 md:mb-0">
            <h3 className={`text-2xl font-bold ${darkMode ? 'text-yellow-400' : 'text-yellow-600'}`}>Titan E-sports</h3>
            <p className={`${darkMode ? 'text-gray-400' : 'text-gray-600'}`}>© 2025 Titan E-sports. All rights reserved.</p>
          </div>
          <div className="flex space-x-6">
            <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-yellow-400' : 'text-gray-600 hover:text-yellow-600'}`}><FaTwitter /></a>
            <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-yellow-400' : 'text-gray-600 hover:text-yellow-600'}`}><FaInstagram /></a>
            <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-yellow-400' : 'text-gray-600 hover:text-yellow-600'}`}><FaFacebookF /></a>
            <a href="https://youtube.com" target="_blank" rel="noopener noreferrer" className={`transition text-xl ${darkMode ? 'text-gray-400 hover:text-yellow-400' : 'text-gray-600 hover:text-yellow-600'}`}><FaYoutube /></a>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default PaymentPage;
