package com.example.cinematesmobile.RetrofitService;

import com.example.cinematesmobile.ModelDBInterno.DBModelDataListeFilmResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelDataUser;
import com.example.cinematesmobile.ModelDBInterno.DBModelFilmsResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelFotoProfiloResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelListeFilmResponse;
import com.example.cinematesmobile.ModelDBInterno.DBModelProfiloAltroUtenteResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelProfiloUtenteResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecensioniResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelUserAmiciResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVotiResponse;

import retrofit2.Call;
import retrofit2.http.Field;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitServiceDBInterno {

    @FormUrlEncoded
    @POST("RicercaUtente.php")
    Call<DBModelDataUser> getUserByQuery(@Field("UserNameDaCercare") String UserNameDaCercare, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("VerificaSeRichiestaPresente.php")
    Call<DBModelVerifica> getVerificaRichiestaDiAmicizia(@Field("Utente") String Utente, @Field("E_Amico_Di") String UserNameAmico);

    @FormUrlEncoded
    @POST("InviaRichiestaDiAmicizia.php")
    Call<DBModelResponseToInsert> InviaRichiestaAmicizia(@Field("Utente") String Utente, @Field("E_Amico_Di") String UserNameAmico);

    @FormUrlEncoded
    @POST("TrovaListeDaVisualizzare.php")
    Call<DBModelListeFilmResponse> getListeFilm(@Field("User_Proprietario") String UserNameAmico);

    @FormUrlEncoded
    @POST("PrendiDaLista.php")
    Call<DBModelFilmsResponce> PrendiFilmDaDB(@Field("User_Proprietario") String Utente, @Field("Tipo_Lista") String TipoLista);

    @FormUrlEncoded
    @POST("InviaSegnalazione.php")
    Call<DBModelResponseToInsert> InviaSegnalazione(@Field("User_Segnalatore") String UtenteSegnalatore, @Field("User_Segnalato") String UtenteSegnalato, @Field("Id_Recensione") String Id_Recensione, @Field("Motivazione") String Motivazione);

    @FormUrlEncoded
    @POST("ListaAmiciInComune.php")
    Call<DBModelUserAmiciResponce> getAmiciInComune(@Field("User_Proprietario") String Username);

    @FormUrlEncoded
    @POST("RimuoviAmico.php")
    Call<DBModelResponseToInsert> RimuoviAmico(@Field("UserProprietario") String UserProprietario, @Field("User_Da_Rimuovere") String User_Da_Rimuovere);

    @FormUrlEncoded
    @POST("VerificaSeSegnalazionePresente.php")
    Call<DBModelVerifica> VerificaPresenzaSegnalazione(@Field("User_Segnalatore") String User_Segnalatore, @Field("User_Segnalato") String User_Segnalato, @Field("Id_Recensione") String Id_Recensione);

    @FormUrlEncoded
    @POST("PrendiRecensioniDaDB.php")
    Call<DBModelRecensioniResponce> PrendiRecensioni(@Field("Titolo_Film_Recensito") String Titolo_Film_Recensito);

    @FormUrlEncoded
    @POST("PrendiFotoUser.php")
    Call<DBModelFotoProfiloResponce> PrendiFotoProfilo(@Field("User") String User);

    @FormUrlEncoded
    @POST("VerificaSeRecensionePresente.php")
    Call<DBModelVerifica> VerificaPresenzaRecensione(@Field("Titolo_Film_Recensito") String Titolo_Film_Recensito, @Field("User_Recensore") String User_Recensore);

    @FormUrlEncoded
    @POST("AggiungiRecensioneAlDatabase.php")
    Call<DBModelResponseToInsert> ScriviRecenisone(@Field("Corpo_Recensione") String Corpo_Recensione, @Field("Valutazione") String Valutazione, @Field("Titolo_Film_Recensito") String Titolo_Film_Recensito, @Field("User_Recensore") String User_Recensore);

    @FormUrlEncoded
    @POST("PrendiMieRecensioniDaDB.php")
    Call<DBModelRecensioniResponce> PrendiMieRecensioni(@Field("UserProprietario") String UserProprietario);

    @FormUrlEncoded
    @POST("RimuoviRecensione.php")
    Call<DBModelResponseToInsert> RimuoviRecensione(@Field("Id_Recensione") String Id_Recensione, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("ListaMieiAmici.php")
    Call<DBModelUserAmiciResponce> getMieiAmici(@Field("User_Proprietario") String Username);

    @FormUrlEncoded
    @POST("RimuoviDaListeFilm.php")
    Call<DBModelResponseToInsert> RimuoviFilm(@Field("Id_Film_Inserito") String Id_Film_Inserito, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("PrendiMediaVoti.php")
    Call<DBModelVotiResponse> PrendiMediaVoti(@Field("Titolo_Film_Recensito") String Titolo_Film_Recensito);

    @FormUrlEncoded
    @POST("PrendiUserDataDaDB.php")
    Call<DBModelProfiloUtenteResponce> PrendiInfoUtente(@Field("Username_Proprietario") String Username_Proprietario);

    @FormUrlEncoded
    @POST("CambiaAttributo.php")
    Call<DBModelResponseToInsert> CambiaInformazioni(@Field("User_Proprietario") String User_Proprietario, @Field("Tipo_Attributo") String Tipo_Attributo, @Field("Nuovo_Attributo") String Nuovo_Attributo);

    @FormUrlEncoded
    @POST("CambiaFotoProfilo.php")
    Call<DBModelResponseToInsert> CambiaFotoProfilo(@Field("User_Proprietario") String User_Proprietario, @Field("Nuova_Foto") String Nuova_Foto);

    @FormUrlEncoded
    @POST("CambiaFotoCopertina.php")
    Call<DBModelResponseToInsert> CambiaFotoCopertina(@Field("User_Proprietario") String User_Proprietario, @Field("Nuova_Foto") String Nuova_Foto);

    @FormUrlEncoded
    @POST("VerificaSeAmico.php")
    Call<DBModelVerifica> VerificaSeAmico(@Field("User_Proprietario") String User_Proprietario, @Field("UsernameAltroUtente") String UsernameAltroUtente);

    @FormUrlEncoded
    @POST("PrendiAltroUserDaDBAmico.php")
    Call<DBModelProfiloAltroUtenteResponce> PrendiAmico(@Field("AltroUser") String AltroUser, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("PrendiAltroUserDaDB.php")
    Call<DBModelProfiloAltroUtenteResponce> PrendiUser(@Field("AltroUser") String AltroUser);

    @FormUrlEncoded
    @POST("PrendiListeFilmAmico.php")
    Call<DBModelDataListeFilmResponce> PrendiFilmAmico(@Field("AltroUser") String AltroUser, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("PrendiListeFilmUser.php")
    Call<DBModelDataListeFilmResponce> PrendiFilmUser(@Field("AltroUser") String AltroUser);
}