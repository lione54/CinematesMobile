package com.example.cinematesmobile.RetrofitService;

import com.example.cinematesmobile.ModelDBInterno.DBModelAttributiLista;
import com.example.cinematesmobile.ModelDBInterno.DBModelCommentiResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelDataListeFilmResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelDataUser;
import com.example.cinematesmobile.ModelDBInterno.DBModelDettagliCinemates;
import com.example.cinematesmobile.ModelDBInterno.DBModelEmojResponde;
import com.example.cinematesmobile.ModelDBInterno.DBModelFilmsResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelFotoProfiloResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelNotificheResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelProfiloAltroUtenteResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelProfiloUtenteResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecensioniResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecuperaPasswdResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecuperaUsername;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelUserAmiciResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
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
    Call<DBModelDataListeFilmResponce> getListeFilm(@Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("PrendiDaLista.php")
    Call<DBModelFilmsResponce> PrendiFilmDaDB(@Field("User_Proprietario") String Utente, @Field("Tipo_Lista") String TipoLista);

    @FormUrlEncoded
    @POST("InviaSegnalazione.php")
    Call<DBModelResponseToInsert> InviaSegnalazione(@Field("User_Segnalatore") String UtenteSegnalatore, @Field("User_Segnalato") String UtenteSegnalato, @Field("Id_Recensione") String Id_Recensione, @Field("Motivazione") String Motivazione);

    @FormUrlEncoded
    @POST("ListaAmiciInComune.php")
    Call<DBModelUserAmiciResponce> getAmiciInComune(@Field("User_Proprietario") String Username, @Field("UserNameCercato") String UserNameCercato);

    @FormUrlEncoded
    @POST("RimuoviAmico.php")
    Call<DBModelResponseToInsert> RimuoviAmico(@Field("UserProprietario") String UserProprietario, @Field("User_Da_Rimuovere") String User_Da_Rimuovere);

    @FormUrlEncoded
    @POST("VerificaSeSegnalazionePresente.php")
    Call<DBModelVerifica> VerificaPresenzaSegnalazione(@Field("User_Segnalatore") String User_Segnalatore, @Field("User_Segnalato") String User_Segnalato, @Field("Id_Recensione") String Id_Recensione);

    @FormUrlEncoded
    @POST("PrendiRecensioniDaDB.php")
    Call<DBModelRecensioniResponce> PrendiRecensioni(@Field("Titolo_Film_Recensito") String Titolo_Film_Recensito, @Field("Tipo") String Tipo, @Field("User_Che_Ha_Scritto_Recensione") String User_Recensore);

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
    Call<DBModelResponseToInsert> RimuoviFilm(@Field("Id_Film_Inserito") String Id_Film_Inserito, @Field("User_Proprietario") String User_Proprietario, @Field("Tipo_Lista") String Tipo_Lista);

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

    @FormUrlEncoded
    @POST("TrovaListe.php")
    Call<DBModelDataListeFilmResponce> TrovaListe(@Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("VerificaSePresente.php")
    Call<DBModelVerifica> VerificaSePresente(@Field("Id_Film_Inserito") String Id_Film_Inserito, @Field("User_Proprietario") String User_Proprietario, @Field("Tipo_Lista") String Tipo_Lista);

    @FormUrlEncoded
    @POST("VerificaSePresenteNeiPreferiti.php")
    Call<DBModelVerifica> VerificaSePresenteNeiPreferiti(@Field("Id_Film_Inserito") String Id_Film_Inserito, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("VerificaSePresenteNeiDaVedere.php")
    Call<DBModelVerifica> VerificaSePresenteNeiDaVedere(@Field("Id_Film_Inserito") String Id_Film_Inserito, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("AggiungiFilmAlDatabase.php")
    Call<DBModelResponseToInsert> AggiungiFilmAlDatabase(@Field("Id_Film_Inserito") String Id_Film_Inserito, @Field("Tipo_Lista") String Tipo_Lista, @Field("User_Proprietario") String User_Proprietario, @Field("Titolo_Film") String Titolo_Film, @Field("Url_Immagine") String Url_Immagine, @Field("Descrizione") String Descrizione, @Field("Visibilita") String Visibilita);

    @FormUrlEncoded
    @POST("RimuoviDaiPreferiti.php")
    Call<DBModelResponseToInsert> RimuoviFilmDaPreferiti(@Field("Id_Film_Inserito") String Id_Film_Inserito, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("RimuoviDaVedere.php")
    Call<DBModelResponseToInsert> RimuoviFilmDaVedere(@Field("Id_Film_Inserito") String Id_Film_Inserito, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("PrendiAttributiLista.php")
    Call<DBModelAttributiLista> getAttributiLista(@Field("User_Proprietario") String User_Proprietario, @Field("Tipo_Lista") String Tipo_Lista);

    @FormUrlEncoded
    @POST("PrendiDettagliCinemates.php")
    Call<DBModelDettagliCinemates> getDettagliCinemates(@Field("Titolo_Film_Recensito") String Titolo_Film_Recensito);

    @FormUrlEncoded
    @POST("PrendiFilmInComune.php")
    Call<DBModelFilmsResponce> PrendiFilmInComune(@Field("AltroUser") String AltroUser, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("VerificaSeHaInseritoEmoj.php")
    Call<DBModelVerifica> VerificaInserimentoEmoj(@Field("User_Proprietario") String User_Proprietario, @Field("Tipo_Corrente") String Tipo_Corrente, @Field("Nome_Lista") String Nome_Lista);

    @FormUrlEncoded
    @POST("PrendiEmojDaDB.php")
    Call<DBModelEmojResponde> PrendiEmojDaDB(@Field("UsernameAltroUtente") String UsernameAltroUtente, @Field("Tipo_Corrente") String Tipo_Corrente, @Field("Nome_Lista") String Nome_Lista, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("EliminaEmoj.php")
    Call<DBModelResponseToInsert> EliminaEmoj(@Field("UsernameAltroUtente") String UsernameAltroUtente, @Field("Tipo_Corrente") String Tipo_Corrente, @Field("Nome_Lista") String Nome_Lista, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("InserisciEmoj.php")
    Call<DBModelResponseToInsert> InserisciEmoj(@Field("UsernameAltroUtente") String UsernameAltroUtente, @Field("Tipo_Corrente") String Tipo_Corrente, @Field("Nome_Lista") String Nome_Lista, @Field("User_Proprietario") String User_Proprietario, @Field("Tipo_Reaction") String Tipo_Reaction);

    @FormUrlEncoded
    @POST("MandaNotificaEmoj.php")
    Call<DBModelResponseToInsert> NotificaInserimentoEmoj(@Field("UsernameAltroUtente") String UsernameAltroUtente,@Field("Tipo_Corrente") String Tipo_Corrente, @Field("Nome_Lista") String Nome_Lista, @Field("Tipo_Reaction") String Tipo_Reaction, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("VerificaSeCiSonoCommenti.php")
    Call<DBModelVerifica> VerificaSeCiSonoCommenti(@Field("AltroUtente") String UsernameAltroUtente, @Field("Tipo_Corrente") String Tipo_Corrente, @Field("Nome_Lista") String Nome_Lista);

    @FormUrlEncoded
    @POST("PrendiCommenti.php")
    Call<DBModelCommentiResponce> PrendiCommenti(@Field("AltroUtente") String UsernameAltroUtente, @Field("Tipo_Corrente") String Tipo_Corrente, @Field("Nome_Lista") String Nome_Lista);

    @FormUrlEncoded
    @POST("AggiungiCommento.php")
    Call<DBModelResponseToInsert> ScriviCommento(@Field("testo_commento") String testo_commento, @Field("Chi_ha_Commentato") String Chi_ha_Commentato, @Field("Dove_ha_Inserito_commento") String Dove_ha_Inserito_commento, @Field("quale_post") String quale_post, @Field("Proprietario_Post") String Proprietario_Post);

    @FormUrlEncoded
    @POST("MandaNotificaCommento.php")
    Call<DBModelResponseToInsert> NotificaInserimentoCommento(@Field("UsernameAltroUtente") String UsernameAltroUtente,  @Field("Tipo_Reaction") String Tipo_Reaction, @Field("Tipo_Corrente") String Tipo_Corrente, @Field("Nome_Lista") String Nome_Lista, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("MandaNotificaRichiestaAmicizia.php")
    Call<DBModelResponseToInsert> NotificaRichiestaAmicizia(@Field("UsernameAltroUtente") String UsernameAltroUtente, @Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("VerificaSeCiSonoNotifiche.php")
    Call<DBModelVerifica> VerificaSeCiSonoNotifiche(@Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("PrendiNotificheDaDB.php")
    Call<DBModelNotificheResponce> PrendiNotificheDaDB(@Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("AccettaAmicizia.php")
    Call<DBModelResponseToInsert> AccettaAmicizia(@Field("UserProprietario") String User_Proprietario, @Field("UserAmico") String UserAmico);

    @FormUrlEncoded
    @POST("RifiutaAmicizia.php")
    Call<DBModelResponseToInsert> RifiutaAmicizia(@Field("UserProprietario") String User_Proprietario, @Field("UserAmico") String UserAmico);

    @FormUrlEncoded
    @POST("DiventaAmico.php")
    Call<DBModelResponseToInsert> DiventaAmico(@Field("UserProprietario") String User_Proprietario, @Field("UserAmico") String UserAmico);

    @FormUrlEncoded
    @POST("SegnaComeLetto.php")
    Call<DBModelResponseToInsert> SegnaComeLetto(@Field("UserProprietario") String User_Proprietario, @Field("TipoNotifica") String TipoNotifica, @Field("QualePost") String QualePost, @Field("Argomento") String Argomento);

    @FormUrlEncoded
    @POST("CodVerifica.php")
    Call<DBModelResponseToInsert> InsertCodVerifica(@Field("User_Proprietario") String User_Proprietario, @Field("EmailProprietario") String EmailProprietario, @Field("CodVerifica") String CodVerifica, @Field("Tipo") String Tipo_Corrente);

    @FormUrlEncoded
    @POST("VerificaEmail.php")
    Call<DBModelVerifica> VerificaEmailInserita(@Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("VerificaPasswd.php")
    Call<DBModelVerifica> VerificaPasswdInserita(@Field("User_Proprietario") String User_Proprietario, @Field("Passwd") String Passwd);

    @FormUrlEncoded
    @POST("RecuperaUsername.php")
    Call<DBModelRecuperaUsername> RecuperaUsername(@Field("User_Proprietario") String User_Proprietario, @Field("Passwd") String Passwd);

    @FormUrlEncoded
    @POST("VerificaCodiceVerifica.php")
    Call<DBModelVerifica> VerificaCodiceVerifica(@Field("User_Proprietario") String User_Proprietario, @Field("Cod_Verifica") String Cod_Verifica);

    @FormUrlEncoded
    @POST("VerificaUsername.php")
    Call<DBModelVerifica> VerificaUsername(@Field("User_Proprietario") String User_Proprietario);

    @FormUrlEncoded
    @POST("InserisciUtente.php")
    Call<DBModelResponseToInsert> InserisciNuovoUtente(@Field("EmailProprietario") String EmailProprietario, @Field("Cod_Verifica") String Cod_Verifica, @Field("Nome") String Nome, @Field("Cognome") String Cognome, @Field("Passwd") String Passwd, @Field("Data_Nascita") String Data_Nascita, @Field("Sesso") String Sesso, @Field("UserName") String UserName);

    @FormUrlEncoded
    @POST("VerificaEmail.php")
    Call<DBModelVerifica> VerificaEmail(@Field("User_Proprietario") String EmailProprietario);

    @FormUrlEncoded
    @POST("ModificaDescrizioneLista.php")
    Call<DBModelResponseToInsert> ModificaDescrizioneLista(@Field("User_Proprietario") String EmailProprietario, @Field("Tipo_Lista") String Tipo_Lista, @Field("Nuova_Descrizione") String Nuova_Descrizione);

    @FormUrlEncoded
    @POST("RecuperaPasswd.php")
    Call<DBModelRecuperaPasswdResponce> RecuperaPasswd(@Field("User_Proprietario") String EmailProprietario);
}
