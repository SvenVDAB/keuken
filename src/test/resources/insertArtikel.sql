insert into artikels(naam, aankoopprijs, verkoopprijs, houdbaarheid, soort, artikelgroepId)
values('testFood', 100, 120, 5, 'F', (select id from artikelgroepen where naam = 'test'));
insert into artikels(naam, aankoopprijs, verkoopprijs, garantie, soort, artikelgroepId)
values('testNonFood', 130, 140, 2, 'NF', (select id from artikelgroepen where naam = 'test'));
insert into kortingen(artikelId, vanafAantal, percentage)
values((select id from artikels where naam = 'testFood'), 10, 5);
insert into kortingen(artikelId, vanafAantal, percentage)
values((select id from artikels where naam = 'testNonFood'), 50, 10);