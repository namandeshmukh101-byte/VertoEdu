import React, { useState, useEffect } from 'react'
import DashboardLayout from '@/layouts/DashboardLayout'
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card'
import { Table, TableHeader, TableRow, TableHead, TableBody, TableCell } from '@/components/ui/table'
import { Skeleton } from '@/components/ui/skeleton'
import api from '@/services/api'

export default function AdminParents() {
  const [parents, setParents] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchParents = async () => {
      try {
        const res = await api.get('/admin/parents/school/1')
        setParents(res.data.data || [])
      } catch (err) {
        console.error('Failed to fetch parents', err)
      } finally {
        setLoading(false)
      }
    }
    fetchParents()
  }, [])

  return (
    <DashboardLayout>
      <div className="mb-8">
        <h1 className="text-3xl font-bold tracking-tight">Manage Parents</h1>
        <p className="text-muted-foreground mt-2">View and manage parent accounts.</p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Parents List</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <Skeleton className="h-48 w-full" />
          ) : parents.length === 0 ? (
            <div className="text-center p-8 text-muted-foreground border-2 border-dashed rounded-lg">
              No parents found.
            </div>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Name</TableHead>
                  <TableHead>Primary Contact</TableHead>
                  <TableHead>Secondary Contact</TableHead>
                  <TableHead>Status</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {parents.map((p) => (
                  <TableRow key={p.id}>
                    <TableCell className="font-medium">{p.firstName} {p.lastName}</TableCell>
                    <TableCell>{p.primaryContactNumber}</TableCell>
                    <TableCell>{p.secondaryContactNumber || '-'}</TableCell>
                    <TableCell>Active</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </DashboardLayout>
  )
}
