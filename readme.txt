# Project overview
this is a homework project to simulate the bet action. there are three parts in the homework
1. get the session for a valid user
        user need to verify his/her session before post stake. if not session is alive or session
        is expired, a new session should be created for the user.

2. post stake with a bet
        user can post his/her bet with a bet offer id and a valid session key.

3. get the top 20 highest stack
        return the top 20 highest bet price associated with the specific bet offer id.

# API description
1. get session
GET {hostname}:{port}/{customerId}/session

2. post a stake
POST {hostname}:{port}/{betOfferId}/stake?sessionKey={sessionKey}
∂
3. get the top 20 highest stakes
GET {hostname}:{port}/{betOfferId}/highstakes

4 the jar file is located under out folder 'out/aritifact'