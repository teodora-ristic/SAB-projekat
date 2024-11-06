
CREATE PROCEDURE SP_AcceptRequest_
	@KorisnickoIme varchar(100)

AS
BEGIN
	declare @RegBroj varchar(100)
	set @RegBroj = (SELECT RegBroj FROM Zahtev WHERE KorisnickoIme=@KorisnickoIme)
	INSERT INTO Kurir(KorisnickoImeKurir,RegBroj,Status,Profit,BrojIsporucenihPaketa) VALUES (@KorisnickoIme,@RegBroj,0,0,0)
	DELETE FROM Zahtev WHERE KorisnickoIme=@KorisnickoIme
END
GO


CREATE TRIGGER TR_TransportOffer_
   ON SastojiSe
   AFTER INSERT
AS 
BEGIN
	declare @kursor cursor
	declare @IdPaketa int

	set @kursor = cursor for
	SELECT IdPaketa FROM inserted 

	open @kursor

	fetch next from @kursor
	into @IdPaketa


	while @@FETCH_STATUS = 0
	begin
		DELETE FROM Ponuda WHERE IdPaketa = @IdPaketa

		fetch next from @kursor
		into @IdPaketa

	end


	close @kursor
	deallocate @kursor


END
GO


