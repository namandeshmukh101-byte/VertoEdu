import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { Card, CardHeader, CardTitle, CardContent } from '../../components/ui/card';
import { Button } from '../../components/ui/button';

const AttendancePage = () => {
  const [classes, setClasses] = useState([]);
  const [selectedSectionId, setSelectedSectionId] = useState('');
  const [date, setDate] = useState(new Date().toISOString().split('T')[0]);
  const [students, setStudents] = useState([]);
  const [attendance, setAttendance] = useState({});
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    api.get('/teacher/me/classes').then(res => setClasses(res.data?.data || [])).catch(console.error);
  }, []);

  const loadStudents = async () => {
    if (!selectedSectionId) return;
    setLoading(true);
    try {
      const res = await api.get(`/teacher/me/classes/${selectedSectionId}/students`);
      setStudents(res.data?.data || []);
      // Try to load existing attendance for today
      const attRes = await api.get(`/teacher/attendance?sectionId=${selectedSectionId}&date=${date}`);
      const existing = {};
      if (attRes.data.data && attRes.data.data.length > 0) {
        attRes.data.data.forEach(a => {
          existing[a.studentId] = { status: a.status, id: a.id };
        });
      }
      setAttendance(existing);
      setMessage(attRes.data.data.length > 0 ? 'Existing attendance loaded' : '');
    } catch (error) {
      setMessage('Failed to load students/attendance');
    } finally {
      setLoading(false);
    }
  };

  const markStatus = (studentId, status) => {
    setAttendance(prev => ({ ...prev, [studentId]: { ...prev[studentId], status } }));
  };

  const saveAttendance = async () => {
    setLoading(true);
    try {
      const promises = students.map(student => {
        const att = attendance[student.id];
        if (!att || !att.status) return Promise.resolve(); // Skip unmarked
        
        const payload = {
          studentId: student.id,
          date: date,
          status: att.status
        };

        if (att.id) {
          return api.put(`/teacher/attendance/${att.id}`, payload);
        } else {
          return api.post('/teacher/attendance', payload);
        }
      });
      await Promise.all(promises);
      setMessage('Attendance saved successfully');
    } catch (error) {
      setMessage('Failed to save attendance. Ensure no duplicates are being created.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <h1 className="text-3xl font-bold">Daily Attendance</h1>

      <Card>
        <CardContent className="pt-6 flex gap-4 items-end">
          <div className="flex-1">
            <label className="block mb-2 text-sm font-medium">Select Class</label>
            <select className="w-full border rounded p-2" value={selectedSectionId} onChange={e => setSelectedSectionId(e.target.value)}>
              <option value="">Select a class</option>
              {classes.map(c => <option key={c.id} value={c.id}>Section {c.name}</option>)}
            </select>
          </div>
          <div className="flex-1">
            <label className="block mb-2 text-sm font-medium">Date</label>
            <input type="date" className="w-full border rounded p-2" value={date} onChange={e => setDate(e.target.value)} />
          </div>
          <Button onClick={loadStudents} disabled={!selectedSectionId || loading}>Load</Button>
        </CardContent>
      </Card>

      {message && <div className="p-4 bg-blue-50 text-blue-700 rounded">{message}</div>}

      {students.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Students</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {students.map(student => (
                <div key={student.id} className="flex justify-between items-center p-4 border rounded bg-gray-50">
                  <div>
                    <div className="font-medium">{student.firstName} {student.lastName}</div>
                    <div className="text-sm text-gray-500">{student.scholarNumber}</div>
                  </div>
                  <div className="flex gap-2">
                    <Button 
                      variant={attendance[student.id]?.status === 'PRESENT' ? 'default' : 'outline'}
                      onClick={() => markStatus(student.id, 'PRESENT')}
                      className={attendance[student.id]?.status === 'PRESENT' ? 'bg-green-600 hover:bg-green-700' : ''}
                    >
                      Present
                    </Button>
                    <Button 
                      variant={attendance[student.id]?.status === 'ABSENT' ? 'default' : 'outline'}
                      onClick={() => markStatus(student.id, 'ABSENT')}
                      className={attendance[student.id]?.status === 'ABSENT' ? 'bg-red-600 hover:bg-red-700' : ''}
                    >
                      Absent
                    </Button>
                  </div>
                </div>
              ))}
            </div>
            <div className="mt-6 flex justify-end">
              <Button onClick={saveAttendance} disabled={loading} size="lg">Save Attendance</Button>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
};

export default AttendancePage;
