// BuyerOrders.jsx - cleaned and fixed
import { useEffect, useState } from 'react';

import { PageHeader, Badge } from '../../components/common/UI';
import ChatModal from '../../components/common/ChatModal';
import RatingModal from '../../components/common/RatingModal';
import { useAuth } from '../../context/AuthContext';
import api, { cancelOrder, confirmDelivery } from '../../api/axios';
import toast from 'react-hot-toast';
import { HiOutlineCube, HiOutlineCalendar, HiOutlineUser, HiOutlineChatAlt2, HiOutlineStar } from 'react-icons/hi';

export default function BuyerOrders() {
  const { token } = useAuth();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(null);

  // Chat Modal State
  const [showChatModal, setShowChatModal] = useState(false);
  const [chatTarget, setChatTarget] = useState(null);

  // Rating Modal State
  const [showRatingModal, setShowRatingModal] = useState(false);
  const [ratingTarget, setRatingTarget] = useState(null);

  const fetchOrders = async () => {
    try {
      const res = await api.get('/buyer/orders');
      if (res.data) {
        setOrders(res.data.data || res.data.orders || (Array.isArray(res.data) ? res.data : []));
      }
    } catch (error) {
      toast.error('Failed to load orders');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (token) fetchOrders();
  }, [token]);

  if (loading) return (
    <div className="flex justify-center py-20">
      <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-forest-600"></div>
    </div>
  );

  return (
    <div className="space-y-6 max-w-4xl mx-auto px-4 py-8">
      <PageHeader
        title="My Purchase Orders"
        subtitle="Track your crop purchases and payment status here."
      />

      {orders.length === 0 ? (
        <div className="card text-center py-20 bg-gray-50 border-dashed">
          <p className="text-gray-500">You haven't placed any orders yet.</p>
        </div>
      ) : (
        <div className="space-y-4">
          {orders.map((order) => (
            <div key={order._id} className="card hover:shadow-md transition-shadow flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
              <div className="space-y-1 flex-1">
                <div className="flex items-center gap-2">
                  <h3 className="font-bold text-lg text-gray-900">{order.listingId?.cropName || 'Crop Listing'}</h3>
                  <Badge variant={order.paymentStatus === 'Paid' ? 'green' : order.paymentStatus === 'Pending' ? 'yellow' : 'blue'}>
                    {order.paymentStatus}
                  </Badge>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-1 text-sm text-gray-500">
                  <p className="flex items-center gap-1.5">
                    <HiOutlineUser className="text-forest-600" />
                    Farmer: <span className="font-medium text-gray-700">{order.farmerId?.name}</span>
                  </p>
                  <p className="flex items-center gap-1.5">
                    <HiOutlineCube className="text-forest-600" />
                    Quantity: <span className="font-medium text-gray-700">{order.quantity?.amount} {order.quantity?.unit}</span>
                  </p>
                  <p className="flex items-center gap-1.5">
                    <HiOutlineCalendar className="text-forest-600" />
                    Ordered: <span className="font-medium text-gray-700">{new Date(order.createdAt).toLocaleDateString()}</span>
                  </p>
                  <p className="text-forest-700 font-bold">
                    Total: ₹{order.totalAmount?.toLocaleString('en-IN')}
                  </p>
                </div>
              </div>

              <div className="flex flex-wrap gap-2 w-full md:w-auto">
                {/* Rate Crop */}
                {order.paymentStatus === 'Paid' && (
                  <button
                    onClick={() => {
                      setRatingTarget({
                        targetId: order.listingId?._id,
                        targetType: 'crop',
                        purchaseId: order._id,
                        cropId: order.listingId?._id,
                        title: `Rate Crop: ${order.listingId?.cropName}`,
                      });
                      setShowRatingModal(true);
                    }}
                    className="btn-outline border-yellow-500 text-yellow-600 hover:bg-yellow-50 text-xs py-2 px-4 flex items-center gap-1"
                  >
                    <HiOutlineStar /> Rate Crop
                  </button>
                )}

                {/* Rate Farmer */}
                {order.paymentStatus === 'Paid' && (
                  <button
                    onClick={() => {
                      setRatingTarget({
                        targetId: order.farmerId?._id,
                        targetType: 'user',
                        purchaseId: order._id,
                        title: `Rate Farmer: ${order.farmerId?.name}`,
                      });
                      setShowRatingModal(true);
                    }}
                    className="btn-outline border-forest-500 text-forest-600 hover:bg-forest-50 text-xs py-2 px-4 flex items-center gap-1"
                  >
                    <HiOutlineStar /> Rate Farmer
                  </button>
                )}

                {/* Cancel Order */}
                {order.orderStatus === 'Pending' && order.paymentStatus === 'Pending' && (
                  <button
                    onClick={async () => {
                      setActionLoading(order._id);
                      try {
                        await cancelOrder(order._id);
                        toast.success('Order cancelled');
                        await fetchOrders();
                      } catch (err) {
                        toast.error(err.response?.data?.message || 'Failed to cancel order');
                      } finally {
                        setActionLoading(null);
                      }
                    }}
                    disabled={actionLoading === order._id}
                    className="btn-primary text-xs py-2 px-4 flex-1 md:flex-none"
                  >
                    {actionLoading === order._id ? 'Cancelling...' : 'Cancel Order'}
                  </button>
                )}

                {/* Confirm Delivery */}
                {order.orderStatus === 'Shipped' && (
                  <button
                    onClick={async () => {
                      setActionLoading(order._id);
                      try {
                        await confirmDelivery(order._id);
                        toast.success('Delivery confirmed');
                        await fetchOrders();
                      } catch (err) {
                        toast.error(err.response?.data?.message || 'Failed to confirm delivery');
                      } finally {
                        setActionLoading(null);
                      }
                    }}
                    disabled={actionLoading === order._id}
                    className="btn-primary text-xs py-2 px-4 flex-1 md:flex-none"
                  >
                    {actionLoading === order._id ? 'Confirming...' : 'Confirm Delivery'}
                  </button>
                )}

                {/* Upload Receipt */}
                {order.paymentStatus === 'Pending' && (
                  <button className="btn-primary text-xs py-2 px-4 flex-1 md:flex-none">Upload Receipt</button>
                )}

                {/* Chat with Farmer */}
                <button
                  onClick={() => {
                    setChatTarget(order);
                    setShowChatModal(true);
                  }}
                  className="w-10 h-10 rounded-xl bg-forest-50 text-forest-600 flex items-center justify-center hover:bg-forest-100 transition-colors"
                  title="Chat with Farmer"
                >
                  <HiOutlineChatAlt2 className="w-5 h-5" />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Chat Modal */}
      {chatTarget && (
        <ChatModal
          open={showChatModal}
          onClose={() => setShowChatModal(false)}
          otherUserId={chatTarget.farmerId?._id}
          cropId={chatTarget.listingId?._id}
          cropName={chatTarget.listingId?.cropName}
          otherUserName={chatTarget.farmerId?.name}
        />
      )}

      {/* Rating Modal */}
      {ratingTarget && (
        <RatingModal
          open={showRatingModal}
          onClose={() => setShowRatingModal(false)}
          {...ratingTarget}
        />
      )}
    </div>
  );
}
