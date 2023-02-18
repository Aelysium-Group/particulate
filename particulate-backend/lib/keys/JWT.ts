import { JwtPayload, Secret, SignOptions, VerifyOptions, decode, sign, verify } from "jsonwebtoken";

export class JWT {
    private _privateKey: Secret;
    private _publicKey: Secret;

    constructor(privateKey: string, publicKey: string) {
        this._privateKey = privateKey;
        this._publicKey = publicKey;
    }

    public sign = (payload: JwtPayload, options: SignOptions) => sign(payload, this._privateKey, options);

    public verify = (token: string, option: VerifyOptions) => {
        const verifyOptions = {
            issuer:  option.issuer,
            subject:  option.subject,
            audience:  option.audience,
            expiresIn:  "30d",
            algorithm:  ["RS256"]
        };

        try {
            return verify(token, this._publicKey, verifyOptions);
        } catch (e) {
            return false;
        }
    }

    /**
     * Decode the JWT Token.
     * @param token The token to decode.
     * @returns A decoded string or `null` if the token is invalid.
     */
    public decode = (token: string) => decode(token, {complete: true});
}