
CREATE TABLE [Administrator]
( 
	[KorisnickoImeAdmin] varchar(100)  NOT NULL 
)
go

CREATE TABLE [Grad]
( 
	[PostanskiBroj]      varchar(100)  NOT NULL ,
	[Naziv]              varchar(50)  NOT NULL ,
	[IdGrada]            integer  IDENTITY  NOT NULL 
)
go

CREATE TABLE [Korisnik]
( 
	[Ime]                varchar(100)  NOT NULL ,
	[Prezime]            varchar(100)  NOT NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[Sifra]              varchar(100)  NOT NULL ,
	[BrojPoslatihPaketa] integer  NOT NULL 
)
go

CREATE TABLE [Kurir]
( 
	[BrojIsporucenihPaketa] integer  NOT NULL ,
	[Profit]             decimal(10,3)  NOT NULL ,
	[Status]             integer  NOT NULL ,
	[KorisnickoImeKurir] varchar(100)  NOT NULL ,
	[RegBroj]            varchar(100)  NOT NULL 
)
go

CREATE TABLE [Opstina]
( 
	[IdOpstine]          integer  IDENTITY  NOT NULL ,
	[Naziv]              varchar(100)  NOT NULL ,
	[X]                  integer  NOT NULL ,
	[Y]                  integer  NOT NULL ,
	[IdGrada]            integer  NOT NULL 
)
go

CREATE TABLE [Paket]
( 
	[IdPaketa]           integer  IDENTITY  NOT NULL ,
	[Status]             integer  NOT NULL ,
	[Cena]               decimal(10,3)  NOT NULL ,
	[VremePrihvatanja]   datetime  NULL ,
	[KorisnickoImeKurir] varchar(100)  NULL ,
	[TipPaketa]          integer  NOT NULL ,
	[TezinaPaketa]       decimal(10,3)  NOT NULL ,
	[IdOpstineDo]        integer  NOT NULL ,
	[IdOpstineOd]        integer  NOT NULL ,
	[KorisnickoIme]      varchar(100)  NOT NULL 
)
go

CREATE TABLE [Ponuda]
( 
	[IdPonude]           integer  IDENTITY  NOT NULL ,
	[ProcenatCeneIsporuke] integer  NOT NULL ,
	[KorisnickoImeKurir] varchar(100)  NOT NULL ,
	[IdPaketa]           integer  NOT NULL 
)
go

CREATE TABLE [SastojiSe]
( 
	[IdVoznje]           integer  NOT NULL ,
	[IdPaketa]           integer  NOT NULL ,
	[IdSastojiSe]        integer  IDENTITY  NOT NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[RegBroj]            varchar(100)  NOT NULL ,
	[TipGoriva]          integer  NOT NULL ,
	[Potrosnja]          decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [Voznja]
( 
	[IdVoznje]           integer  IDENTITY  NOT NULL ,
	[KorisnickoImeKurir] varchar(100)  NOT NULL 
)
go

CREATE TABLE [Zahtev]
( 
	[KorisnickoIme]      varchar(100)  NOT NULL ,
	[RegBroj]            varchar(100)  NOT NULL 
)
go

ALTER TABLE [Administrator]
	ADD CONSTRAINT [XPKAdministrator] PRIMARY KEY  CLUSTERED ([KorisnickoImeAdmin] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdGrada] ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([KorisnickoImeKurir] ASC)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XPKOpstina] PRIMARY KEY  CLUSTERED ([IdOpstine] ASC)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([IdPaketa] ASC)
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [XPKPonuda] PRIMARY KEY  CLUSTERED ([IdPonude] ASC)
go

ALTER TABLE [SastojiSe]
	ADD CONSTRAINT [XPKSastojiSe] PRIMARY KEY  CLUSTERED ([IdSastojiSe] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([RegBroj] ASC)
go

ALTER TABLE [Voznja]
	ADD CONSTRAINT [XPKVoznja] PRIMARY KEY  CLUSTERED ([IdVoznje] ASC)
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [XPKZahtev] PRIMARY KEY  CLUSTERED ([KorisnickoIme] ASC)
go


ALTER TABLE [Administrator]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([KorisnickoImeAdmin]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([KorisnickoImeKurir]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([RegBroj]) REFERENCES [Vozilo]([RegBroj])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdGrada]) REFERENCES [Grad]([IdGrada])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_31] FOREIGN KEY ([KorisnickoImeKurir]) REFERENCES [Kurir]([KorisnickoImeKurir])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_32] FOREIGN KEY ([IdOpstineDo]) REFERENCES [Opstina]([IdOpstine])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_33] FOREIGN KEY ([IdOpstineOd]) REFERENCES [Opstina]([IdOpstine])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_37] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([KorisnickoImeKurir]) REFERENCES [Kurir]([KorisnickoImeKurir])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_22] FOREIGN KEY ([IdPaketa]) REFERENCES [Paket]([IdPaketa])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [SastojiSe]
	ADD CONSTRAINT [R_29] FOREIGN KEY ([IdVoznje]) REFERENCES [Voznja]([IdVoznje])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [SastojiSe]
	ADD CONSTRAINT [R_38] FOREIGN KEY ([IdPaketa]) REFERENCES [Paket]([IdPaketa])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Voznja]
	ADD CONSTRAINT [R_39] FOREIGN KEY ([KorisnickoImeKurir]) REFERENCES [Kurir]([KorisnickoImeKurir])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_34] FOREIGN KEY ([KorisnickoIme]) REFERENCES [Korisnik]([KorisnickoIme])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Zahtev]
	ADD CONSTRAINT [R_36] FOREIGN KEY ([RegBroj]) REFERENCES [Vozilo]([RegBroj])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go
