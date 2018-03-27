<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sign In</title>
</head>
<body>
    <form action="/user/signinvalidate" method="POST">
      
        <h1>Please sign in</h1>
        <input name="username" id="inputUsername" class="form-control mb-3" placeholder="Username" required="" autofocus="" type="text">
        <input name="password" id="inputPassword" class="form-control" placeholder="Password" required="" type="password">
        <button  type="submit">Sign in</button>
        <p >&copy;CSYE6225</p>
    </form>
</body>
</html>