#!/bin/bash

# API Security Guardian - Test Script
set -e

echo "🧪 Running tests..."
echo ""

# Backend tests
echo "📦 Backend tests..."
cd backend
./mvnw test
echo "✅ Backend tests passed"
echo ""

# Frontend tests
echo "🎨 Frontend tests..."
cd ../frontend
npm run test
echo "✅ Frontend tests passed"
echo ""

# E2E tests
echo "🔄 E2E tests..."
npm run test:e2e
echo "✅ E2E tests passed"
echo ""

echo "🎉 All tests passed!"

