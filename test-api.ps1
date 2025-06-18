# Phone Management API Test Script
Write-Host "=== Phone Management API Test ===" -ForegroundColor Green

# Test 1: Login as Admin
Write-Host "`n1. Testing Admin Login..." -ForegroundColor Yellow
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    Write-Host "✅ Login successful!" -ForegroundColor Green
    Write-Host "   Username: $($loginResponse.username)" -ForegroundColor Cyan
    Write-Host "   Role: $($loginResponse.role)" -ForegroundColor Cyan
    Write-Host "   Token: $($loginResponse.token.Substring(0,20))..." -ForegroundColor Cyan
    
    $token = $loginResponse.token
    $headers = @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }
} catch {
    Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit
}

# Test 2: Get Available Roles
Write-Host "`n2. Testing Get Roles..." -ForegroundColor Yellow
try {
    $rolesResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/roles" -Method GET -Headers $headers
    Write-Host "✅ Roles retrieved successfully!" -ForegroundColor Green
    foreach ($role in $rolesResponse) {
        Write-Host "   Role ID: $($role.id), Name: $($role.name)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Get roles failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Get System Users
Write-Host "`n3. Testing Get System Users..." -ForegroundColor Yellow
try {
    $usersResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/system-users" -Method GET -Headers $headers
    Write-Host "✅ System users retrieved successfully!" -ForegroundColor Green
    foreach ($user in $usersResponse) {
        Write-Host "   User ID: $($user.id), Username: $($user.username), Role: $($user.role)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Get system users failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Create New System User
Write-Host "`n4. Testing Create System User..." -ForegroundColor Yellow
$newUserBody = @{
    username = "testuser"
    email = "testuser@example.com"
    password = "testpass123"
    roleId = 2  # ASSIGNER role
} | ConvertTo-Json

try {
    $createResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/system-users" -Method POST -Body $newUserBody -Headers $headers
    Write-Host "✅ User created successfully!" -ForegroundColor Green
    Write-Host "   New User ID: $($createResponse.id)" -ForegroundColor Cyan
    Write-Host "   Username: $($createResponse.username)" -ForegroundColor Cyan
    Write-Host "   Role: $($createResponse.role)" -ForegroundColor Cyan
    
    $newUserId = $createResponse.id
} catch {
    Write-Host "❌ Create user failed: $($_.Exception.Message)" -ForegroundColor Red
    $newUserId = $null
}

# Test 5: Update System User (if created)
if ($newUserId) {
    Write-Host "`n5. Testing Update System User..." -ForegroundColor Yellow
    $updateUserBody = @{
        username = "updateduser"
        email = "updateduser@example.com"
        password = "newpass123"
        roleId = 1  # ADMIN role
    } | ConvertTo-Json

    try {
        $updateResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/system-users/$newUserId" -Method PUT -Body $updateUserBody -Headers $headers
        Write-Host "✅ User updated successfully!" -ForegroundColor Green
        Write-Host "   Updated Username: $($updateResponse.username)" -ForegroundColor Cyan
        Write-Host "   Updated Role: $($updateResponse.role)" -ForegroundColor Cyan
    } catch {
        Write-Host "❌ Update user failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 6: Get Phones
Write-Host "`n6. Testing Get Phones..." -ForegroundColor Yellow
try {
    $phonesResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/phones" -Method GET -Headers $headers
    Write-Host "✅ Phones retrieved successfully!" -ForegroundColor Green
    Write-Host "   Total phones: $($phonesResponse.content.Count)" -ForegroundColor Cyan
    foreach ($phone in $phonesResponse.content) {
        Write-Host "   Phone ID: $($phone.id), Brand: $($phone.brand), Model: $($phone.model)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Get phones failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 7: Get Assignments
Write-Host "`n7. Testing Get Assignments..." -ForegroundColor Yellow
try {
    $assignmentsResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/assignments" -Method GET -Headers $headers
    Write-Host "✅ Assignments retrieved successfully!" -ForegroundColor Green
    Write-Host "   Total assignments: $($assignmentsResponse.content.Count)" -ForegroundColor Cyan
    foreach ($assignment in $assignmentsResponse.content) {
        Write-Host "   Assignment ID: $($assignment.id), User: $($assignment.user.firstName) $($assignment.user.lastName)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Get assignments failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== API Test Complete ===" -ForegroundColor Green 