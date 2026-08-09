import React from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { Card, CardHeader, CardTitle, CardContent } from '../../components/ui/card';

const ProfilePage = () => {
  const { user } = useAuth();

  return (
    <div className="p-8 max-w-4xl mx-auto space-y-6">
      <h1 className="text-3xl font-bold">Teacher Profile</h1>
      
      <Card>
        <CardHeader>
          <CardTitle>Personal Details</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <div className="text-sm text-gray-500 mb-1">Full Name</div>
              <div className="font-medium text-lg">{user?.fullName}</div>
            </div>
            <div>
              <div className="text-sm text-gray-500 mb-1">Email Address</div>
              <div className="font-medium text-lg">{user?.email}</div>
            </div>
            <div>
              <div className="text-sm text-gray-500 mb-1">Role</div>
              <div className="font-medium text-lg">{user?.role}</div>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default ProfilePage;
