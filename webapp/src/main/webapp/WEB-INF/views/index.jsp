<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    
    <title>Index</title>
</head>
<body >
    <div >
      
        <h1 >Welcome!</h1>
        <form action="/user/showprofile" method="GET">
            <input type="text" id="username" name="username" placeholder="search profile"><br/>
            <button type="submit" onclick="window.location.href='/user/showprofile'">Search</button>
        </form>
        <button type="submit" onclick="window.location.href='/user/signin'">Sign in</button>
        <button type="submit" onclick="window.location.href='/user/signup'">Sign up</button>
        <p>CSYE6225</p>
    </div>
</body>
</html>