import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { Card, CardHeader, CardTitle, CardContent } from '../../components/ui/card';
import { Button } from '../../components/ui/button';

const MarksEntryPage = () => {
  const [classes, setClasses] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [exams, setExams] = useState([]);
  
  const [selectedSectionId, setSelectedSectionId] = useState('');
  const [selectedSubjectId, setSelectedSubjectId] = useState('');
  const [selectedExamId, setSelectedExamId] = useState('');
  
  const [students, setStudents] = useState([]);
  const [marks, setMarks] = useState({});
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    // Initial data fetch
    api.get('/teacher/me/classes').then(res => setClasses(res.data?.data || [])).catch(console.error);
    api.get('/teacher/me/subjects').then(res => setSubjects(res.data?.data || [])).catch(console.error);
    // Hardcoding academicYearId to 1 for MVP (or fetch from a global context)
    api.get('/teacher/exams?academicYearId=1').then(res => setExams(res.data?.data || [])).catch(console.error);
  }, []);

  const loadStudentsAndMarks = async () => {
    if (!selectedSectionId || !selectedSubjectId || !selectedExamId) return;
    setLoading(true);
    try {
      const res = await api.get(`/teacher/me/classes/${selectedSectionId}/students`);
      setStudents(res.data?.data || []);
      
      const marksRes = await api.get(`/teacher/results?sectionId=${selectedSectionId}&subjectId=${selectedSubjectId}&examId=${selectedExamId}`);
      const existing = {};
      if (marksRes.data.data) {
        marksRes.data.data.forEach(m => {
          existing[m.studentId] = { marksObtained: m.marksObtained, maxMarks: m.maxMarks, id: m.id };
        });
      }
      setMarks(existing);
      setMessage(marksRes.data.data.length > 0 ? 'Existing marks loaded' : '');
    } catch (error) {
      setMessage('Failed to load students/marks');
    } finally {
      setLoading(false);
    }
  };

  const handleMarkChange = (studentId, field, value) => {
    setMarks(prev => ({ ...prev, [studentId]: { ...prev[studentId], [field]: value } }));
  };

  const saveMarks = async () => {
    setLoading(true);
    setMessage('');
    try {
      const promises = students.map(student => {
        const m = marks[student.id];
        if (!m || m.marksObtained === undefined || m.maxMarks === undefined) return Promise.resolve(); 
        
        const payload = {
          studentId: student.id,
          subjectId: selectedSubjectId,
          examId: selectedExamId,
          marksObtained: parseFloat(m.marksObtained),
          maxMarks: parseFloat(m.maxMarks)
        };

        if (m.id) {
          return api.put(`/teacher/results/${m.id}`, payload);
        } else {
          return api.post('/teacher/results', payload);
        }
      });
      await Promise.all(promises);
      setMessage('Marks saved successfully');
    } catch (error) {
      setMessage('Failed to save marks. Check that marks do not exceed maximum.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <h1 className="text-3xl font-bold">Exam Results Entry</h1>

      <Card>
        <CardContent className="pt-6 grid md:grid-cols-4 gap-4 items-end">
          <div>
            <label className="block mb-2 text-sm font-medium">Class</label>
            <select className="w-full border rounded p-2" value={selectedSectionId} onChange={e => setSelectedSectionId(e.target.value)}>
              <option value="">Select class</option>
              {classes.map(c => <option key={c.id} value={c.id}>Section {c.name}</option>)}
            </select>
          </div>
          <div>
            <label className="block mb-2 text-sm font-medium">Subject</label>
            <select className="w-full border rounded p-2" value={selectedSubjectId} onChange={e => setSelectedSubjectId(e.target.value)}>
              <option value="">Select subject</option>
              {subjects.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
            </select>
          </div>
          <div>
            <label className="block mb-2 text-sm font-medium">Exam</label>
            <select className="w-full border rounded p-2" value={selectedExamId} onChange={e => setSelectedExamId(e.target.value)}>
              <option value="">Select exam</option>
              {exams.map(e => <option key={e.id} value={e.id}>{e.name}</option>)}
            </select>
          </div>
          <Button onClick={loadStudentsAndMarks} disabled={!selectedSectionId || !selectedSubjectId || !selectedExamId || loading}>Load</Button>
        </CardContent>
      </Card>

      {message && <div className="p-4 bg-blue-50 text-blue-700 rounded">{message}</div>}

      {students.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Students List</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {students.map(student => (
                <div key={student.id} className="flex justify-between items-center p-4 border rounded bg-gray-50">
                  <div className="flex-1">
                    <div className="font-medium">{student.firstName} {student.lastName}</div>
                    <div className="text-sm text-gray-500">{student.scholarNumber}</div>
                  </div>
                  <div className="flex gap-4 items-center">
                    <div>
                      <label className="text-xs text-gray-500 block">Obtained</label>
                      <input 
                        type="number" 
                        className="border p-2 rounded w-24" 
                        value={marks[student.id]?.marksObtained || ''}
                        onChange={e => handleMarkChange(student.id, 'marksObtained', e.target.value)}
                      />
                    </div>
                    <div className="text-xl">/</div>
                    <div>
                      <label className="text-xs text-gray-500 block">Max Marks</label>
                      <input 
                        type="number" 
                        className="border p-2 rounded w-24" 
                        value={marks[student.id]?.maxMarks || ''}
                        onChange={e => handleMarkChange(student.id, 'maxMarks', e.target.value)}
                      />
                    </div>
                  </div>
                </div>
              ))}
            </div>
            <div className="mt-6 flex justify-end">
              <Button onClick={saveMarks} disabled={loading} size="lg">Save Marks</Button>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  );
};

export default MarksEntryPage;
