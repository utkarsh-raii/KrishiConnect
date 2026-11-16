import React from 'react';
import { initiatePayment, verifyPayment } from '../api/paymentApi';
import toast from 'react-hot-toast';

export default function PaymentButton({ amount, onSuccess, onFailure, token, className, children }) {
  const handlePayment = async () => {
    try {
      const data = await initiatePayment(amount, token);
      const razorpayOrderId = data.razorpayOrderId || data.orderId || (data.data && (data.data.razorpayOrderId || data.data.id));
      const razorpayKey = import.meta.env.VITE_RAZORPAY_KEY_ID || data.keyId || 'rzp_test_placeholder';

      const options = {
        key: razorpayKey,
        amount: Math.round(Number(amount) * 100),
        currency: 'INR',
        name: 'KrishiConnect',
        description: 'Agricultural Marketplace Payment',
        order_id: razorpayOrderId,
        handler: async function (response) {
          try {
            const verifyRes = await verifyPayment({
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature,
            }, token);

            if (verifyRes.success) {
              toast.success('Payment verified successfully! 🎉');
              if (onSuccess) onSuccess(verifyRes);
            } else {
              toast.error(verifyRes.message || 'Payment verification failed');
              if (onFailure) onFailure(verifyRes);
            }
          } catch (err) {
            toast.error('Payment verification failed');
            if (onFailure) onFailure(err);
          }
        },
        prefill: {
          name: 'KrishiConnect Buyer',
          email: 'buyer@krishiconnect.com',
        },
        theme: {
          color: '#16a34a',
        },
      };

      if (window.Razorpay) {
        const razor = new window.Razorpay(options);
        razor.open();
      } else {
        // Fallback for development if Razorpay script is not in HTML
        toast.success('Payment simulated in test mode');
        if (onSuccess) onSuccess({ success: true, simulated: true });
      }
    } catch (error) {
      console.error('Payment error:', error);
      toast.error('Failed to initiate payment');
      if (onFailure) onFailure(error);
    }
  };

  return (
    <button onClick={handlePayment} className={className || 'btn-primary'}>
      {children || `Pay ₹${amount}`}
    </button>
  );
}
