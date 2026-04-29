import { useEffect, useMemo, useState } from 'react';

const API_BASE = 'http://localhost:8080/students';

const emptyStudent = {
  name: '',
  email: '',
  course: ''
};

export default function App() {
  const [students, setStudents] = useState([]);
  const [form, setForm] = useState(emptyStudent);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [deletingId, setDeletingId] = useState(null);

  const title = useMemo(() => (editingId ? 'Edit student' : 'Add student'), [editingId]);

  useEffect(() => {
    loadStudents();
  }, []);

  async function loadStudents() {
    setLoading(true);
    setError('');
    try {
      const response = await fetch(API_BASE);
      if (!response.ok) {
        throw new Error(`Failed to load students (${response.status})`);
      }
      const data = await response.json();
      setStudents(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || 'Could not load students');
    } finally {
      setLoading(false);
    }
  }

  function handleChange(event) {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  }

  function resetForm() {
    setForm(emptyStudent);
    setEditingId(null);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setError('');
    setMessage('');

    const payload = {
      name: form.name.trim(),
      email: form.email.trim(),
      course: form.course.trim()
    };

    try {
      const response = await fetch(editingId ? `${API_BASE}/${editingId}` : API_BASE, {
        method: editingId ? 'PUT' : 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
      });

      const text = await response.text();
      if (!response.ok) {
        throw new Error(text || 'Request failed');
      }

      setMessage(editingId ? 'Student updated successfully.' : 'Student created successfully.');
      resetForm();
      await loadStudents();
    } catch (err) {
      setError(err.message || 'Something went wrong');
    } finally {
      setSaving(false);
    }
  }

  function handleEdit(student) {
    setEditingId(student.id);
    setForm({
      name: student.name || '',
      email: student.email || '',
      course: student.course || ''
    });
    setMessage('');
    setError('');
  }

  async function handleDelete(id) {
    const confirmed = window.confirm('Delete this student?');
    if (!confirmed) {
      return;
    }

    setDeletingId(id);
    setError('');
    setMessage('');

    try {
      const response = await fetch(`${API_BASE}/${id}`, {
        method: 'DELETE'
      });

      const text = await response.text();
      if (!response.ok) {
        throw new Error(text || 'Delete failed');
      }

      setMessage('Student deleted successfully.');
      await loadStudents();
    } catch (err) {
      setError(err.message || 'Could not delete student');
    } finally {
      setDeletingId(null);
    }
  }

  return (
    <div className="app-shell">
      <div className="aurora aurora-left" />
      <div className="aurora aurora-right" />
      <div className="aurora aurora-center" />

      <main className="card">
        <div className="hero">
          <div>
            <p className="eyebrow">Spring Boot + React</p>
            <h1>Student CRUD Dashboard</h1>
            <p className="subcopy">
              Create, edit, delete, and refresh students from your Spring API in one place.
            </p>
          </div>
          <button className="ghost-button" type="button" onClick={loadStudents} disabled={loading}>
            {loading ? 'Loading...' : 'Refresh data'}
          </button>
        </div>

        <div className="content-grid">
          <form className="panel form-panel" onSubmit={handleSubmit}>
            <div className="panel-header">
              <div>
                <p className="panel-kicker">Student form</p>
                <h2>{title}</h2>
              </div>
              {editingId && (
                <button type="button" className="link-button" onClick={resetForm}>
                  Cancel edit
                </button>
              )}
            </div>

            <label>
              <span>Name</span>
              <input name="name" value={form.name} onChange={handleChange} placeholder="Jane Doe" required />
            </label>

            <label>
              <span>Email</span>
              <input
                name="email"
                type="email"
                value={form.email}
                onChange={handleChange}
                placeholder="jane@example.com"
                required
              />
            </label>

            <label>
              <span>Course</span>
              <input name="course" value={form.course} onChange={handleChange} placeholder="Computer Science" required />
            </label>

            <div className="form-actions">
              <button className="primary-button" type="submit" disabled={saving}>
                {saving ? 'Saving...' : editingId ? 'Update student' : 'Create student'}
              </button>
              <button className="secondary-button" type="button" onClick={resetForm}>
                Clear
              </button>
            </div>
          </form>

          <div className="panel table-panel">
            <div className="panel-header">
              <div>
                <p className="panel-kicker">Saved records</p>
                <h2>Students</h2>
              </div>
              <span className="count-pill">{students.length} total</span>
            </div>

            {message && <div className="status success">{message}</div>}
            {error && <div className="status error">{error}</div>}

            {loading ? (
              <div className="empty-state">Loading students...</div>
            ) : students.length === 0 ? (
              <div className="empty-state">
                No students yet. Add one with the form on the left.
              </div>
            ) : (
              <div className="table-wrap">
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Email</th>
                      <th>Course</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {students.map((student) => (
                      <tr key={student.id}>
                        <td>{student.id}</td>
                        <td>{student.name}</td>
                        <td>{student.email}</td>
                        <td>{student.course}</td>
                        <td>
                          <div className="row-actions">
                            <button type="button" className="mini-button" onClick={() => handleEdit(student)}>
                              Edit
                            </button>
                            <button type="button" className="mini-button danger" onClick={() => handleDelete(student.id)} disabled={deletingId === student.id}>
                              {deletingId === student.id ? '...' : 'Delete'}
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}